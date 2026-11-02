package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.StudyCommandApplication
import com.example.alarm.AlarmScheduler
import com.example.data.DefaultData
import com.example.data.model.*
import com.example.data.repository.StudyRepository
import com.example.receiver.AlarmReceiver
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

enum class ScreenTab(val title: String) {
    DASHBOARD("Dashboard"),
    CALENDAR("Schedule"),
    SUBJECTS("Syllabus"),
    REVISION("Revision"),
    ANALYTICS("Analytics"),
    ALARMS("Alarms"),
    SETTINGS("Settings")
}

data class DashboardMetrics(
    val countdownDays: Long = 33,
    val totalUnits: Int = 35,
    val completedUnits: Int = 0,
    val pendingUnits: Int = 35,
    val syllabusPercent: Float = 0f,
    val revision1Percent: Float = 0f,
    val revision2Percent: Float = 0f,
    val overallScorePercent: Float = 0f,
    val completedHours: Float = 0f,
    val streakDays: Int = 1,
    val isDailyBadgeUnlocked: Boolean = false,
    val nextAlarm: AlarmItemEntity? = null
)

class StudyViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as StudyCommandApplication
    private val repository = StudyRepository(app.database.studyDao(), app.alarmScheduler)

    // Navigation state
    private val _currentTab = MutableStateFlow(ScreenTab.DASHBOARD)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    fun navigateTo(tab: ScreenTab) {
        _currentTab.value = tab
    }

    // Selected date for Calendar & Today's target (default 2026-11-03 or current date if in range)
    private val _selectedDate = MutableStateFlow("2026-11-03")
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    fun setSelectedDate(dateStr: String) {
        _selectedDate.value = dateStr
    }

    // Rotating motivation quote index
    private val _quoteIndex = MutableStateFlow(0)
    val currentQuote: StateFlow<String> = _quoteIndex.map {
        DefaultData.motivationalQuotes[it % DefaultData.motivationalQuotes.size]
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DefaultData.motivationalQuotes[0])

    fun rotateQuote() {
        _quoteIndex.value = (_quoteIndex.value + 1) % DefaultData.motivationalQuotes.size
    }

    // Repository flows
    val subjects: StateFlow<List<SubjectEntity>> = repository.allSubjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val units: StateFlow<List<StudyUnitEntity>> = repository.allUnits
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<ScheduleTaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alarms: StateFlow<List<AlarmItemEntity>> = repository.allAlarms
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studyLogs: StateFlow<List<StudyLogEntity>> = repository.allStudyLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Today's tasks (reactive for selectedDate)
    val tasksForSelectedDate: StateFlow<List<ScheduleTaskEntity>> =
        combine(tasks, _selectedDate) { allTasks, date ->
            allTasks.filter { it.dateStr == date }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Backlog tasks (past date tasks not yet completed)
    val backlogTasks: StateFlow<List<ScheduleTaskEntity>> =
        combine(tasks, _selectedDate) { allTasks, date ->
            allTasks.filter { it.dateStr < date && !it.isCompleted }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Aggregated Dashboard Metrics
    val dashboardMetrics: StateFlow<DashboardMetrics> = combine(
        units,
        tasks,
        alarms,
        studyLogs,
        _selectedDate
    ) { unitList, taskList, alarmList, logs, selDate ->
        val total = if (unitList.isNotEmpty()) unitList.size else 35
        val completedUnits = unitList.count { it.isCompleted }
        val pending = total - completedUnits

        val sylPercent = if (total > 0) (completedUnits.toFloat() / total) * 100f else 0f
        val r1Count = unitList.count { it.revision1Completed }
        val r1Percent = if (total > 0) (r1Count.toFloat() / total) * 100f else 0f

        val r2Count = unitList.count { it.revision2Completed }
        val r2Percent = if (total > 0) (r2Count.toFloat() / total) * 100f else 0f

        val overallScore = (sylPercent * 0.5f) + (r1Percent * 0.3f) + (r2Percent * 0.2f)

        // Count completed study hours strictly from completed sessions
        val totalMinutes = logs.sumOf { it.durationMinutes }
        val completedHours = totalMinutes / 60f

        // Countdown to 6 Dec 2026
        val targetDate = LocalDate.of(2026, 12, 6)
        val today = try {
            LocalDate.parse(selDate)
        } catch (_: Exception) {
            LocalDate.of(2026, 11, 3)
        }
        val daysRemaining = ChronoUnit.DAYS.between(today, targetDate).coerceAtLeast(0)

        // Today's tasks completion badge
        val todayTasks = taskList.filter { it.dateStr == selDate }
        val allDoneToday = todayTasks.isNotEmpty() && todayTasks.all { it.isCompleted }

        // Next upcoming alarm
        val next = AlarmScheduler.getNextUpcomingAlarm(alarmList)

        DashboardMetrics(
            countdownDays = daysRemaining,
            totalUnits = total,
            completedUnits = completedUnits,
            pendingUnits = pending,
            syllabusPercent = sylPercent,
            revision1Percent = r1Percent,
            revision2Percent = r2Percent,
            overallScorePercent = overallScore,
            completedHours = completedHours,
            streakDays = if (allDoneToday) 4 else 3,
            isDailyBadgeUnlocked = allDoneToday,
            nextAlarm = next
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardMetrics())

    // --- ACTIONS ---

    fun toggleTask(task: ScheduleTaskEntity) {
        viewModelScope.launch {
            repository.toggleTaskComplete(task, !task.isCompleted)
        }
    }

    fun rescheduleTask(taskId: String, newDateStr: String, newTimeSlot: String) {
        viewModelScope.launch {
            repository.rescheduleTask(taskId, newDateStr, newTimeSlot)
        }
    }

    fun addCustomTask(
        dateStr: String,
        timeSlot: String,
        subjectId: String,
        subjectName: String,
        taskTitle: String,
        phase: SchedulePhase
    ) {
        viewModelScope.launch {
            val newTask = ScheduleTaskEntity(
                id = "CUSTOM_${System.currentTimeMillis()}",
                dateStr = dateStr,
                timeSlot = timeSlot,
                subjectId = subjectId,
                subjectName = subjectName,
                taskTitle = taskTitle,
                phase = phase,
                durationMinutes = 120
            )
            repository.insertTask(newTask)
        }
    }

    fun deleteTask(task: ScheduleTaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    // Units
    fun toggleUnit(unit: StudyUnitEntity) {
        viewModelScope.launch {
            repository.toggleUnitCompletion(unit)
        }
    }

    fun toggleRevision1(unit: StudyUnitEntity) {
        viewModelScope.launch {
            repository.toggleRevision1(unit)
        }
    }

    fun toggleRevision2(unit: StudyUnitEntity) {
        viewModelScope.launch {
            repository.toggleRevision2(unit)
        }
    }

    fun toggleWeakArea(unit: StudyUnitEntity) {
        viewModelScope.launch {
            repository.toggleWeakArea(unit)
        }
    }

    fun updateUnitDetails(unit: StudyUnitEntity, title: String, notes: String, topics: String) {
        viewModelScope.launch {
            repository.updateUnit(
                unit.copy(
                    title = title,
                    personalNotes = notes,
                    importantTopics = topics
                )
            )
        }
    }

    fun addCustomUnit(
        subjectId: String,
        unitNumber: Int,
        title: String,
        topics: String,
        notes: String
    ) {
        viewModelScope.launch {
            val newUnit = StudyUnitEntity(
                id = "${subjectId}_U${System.currentTimeMillis() % 10000}",
                subjectId = subjectId,
                unitNumber = unitNumber,
                title = title,
                importantTopics = topics,
                personalNotes = notes
            )
            repository.insertUnit(newUnit)
        }
    }

    fun deleteUnit(unit: StudyUnitEntity) {
        viewModelScope.launch {
            repository.deleteUnit(unit)
        }
    }

    // Alarms
    fun toggleAlarm(alarm: AlarmItemEntity) {
        viewModelScope.launch {
            repository.toggleAlarm(alarm, !alarm.isEnabled)
        }
    }

    fun saveAlarm(alarm: AlarmItemEntity) {
        viewModelScope.launch {
            repository.saveAlarm(alarm)
        }
    }

    fun deleteAlarm(alarm: AlarmItemEntity) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
        }
    }

    fun triggerAlarmTest(alarm: AlarmItemEntity, context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_ALARM_ID, alarm.id)
            putExtra(AlarmReceiver.EXTRA_ALARM_TITLE, alarm.title)
            putExtra(AlarmReceiver.EXTRA_ALARM_TIME, alarm.timeStr)
            putExtra(AlarmReceiver.EXTRA_ALARM_SOUND, alarm.soundName)
        }
        context.sendBroadcast(intent)
    }

    // Profile & Settings
    fun updateProfile(name: String, examDate: String, isDarkMode: Boolean) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            repository.updateUserProfile(
                current.copy(
                    studentName = name,
                    targetExamDateStr = examDate,
                    isDarkMode = isDarkMode
                )
            )
        }
    }

    fun toggleDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            val current = userProfile.value ?: UserProfileEntity()
            repository.updateUserProfile(current.copy(isDarkMode = isDark))
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllProgress()
        }
    }
}
