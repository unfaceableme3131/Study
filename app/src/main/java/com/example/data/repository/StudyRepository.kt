package com.example.data.repository

import com.example.alarm.AlarmScheduler
import com.example.data.dao.StudyDao
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class StudyRepository(
    private val dao: StudyDao,
    private val alarmScheduler: AlarmScheduler
) {
    val allSubjects: Flow<List<SubjectEntity>> = dao.getAllSubjects()
    val allUnits: Flow<List<StudyUnitEntity>> = dao.getAllUnits()
    val allTasks: Flow<List<ScheduleTaskEntity>> = dao.getAllTasks()
    val allAlarms: Flow<List<AlarmItemEntity>> = dao.getAllAlarms()
    val allStudyLogs: Flow<List<StudyLogEntity>> = dao.getAllStudyLogs()
    val userProfile: Flow<UserProfileEntity?> = dao.getUserProfile()

    fun getUnitsForSubject(subjectId: String): Flow<List<StudyUnitEntity>> =
        dao.getUnitsForSubject(subjectId)

    fun getTasksForDate(dateStr: String): Flow<List<ScheduleTaskEntity>> =
        dao.getTasksForDate(dateStr)

    fun getBacklogTasks(todayDateStr: String): Flow<List<ScheduleTaskEntity>> =
        dao.getBacklogTasks(todayDateStr)

    suspend fun toggleTaskComplete(task: ScheduleTaskEntity, completed: Boolean) {
        dao.updateTaskCompletion(task.id, completed)

        // Automatically log completed study session (120 min)
        if (completed) {
            dao.insertStudyLog(
                StudyLogEntity(
                    dateStr = task.dateStr,
                    durationMinutes = task.durationMinutes,
                    subjectName = task.subjectName,
                    taskTitle = task.taskTitle
                )
            )

            // If task is linked to a unit, update unit completion / revision status
            if (task.unitId.isNotBlank()) {
                val unit = dao.getUnitById(task.unitId)
                if (unit != null) {
                    when (task.phase) {
                        SchedulePhase.SYLLABUS_COMPLETION -> {
                            dao.updateUnit(unit.copy(isCompleted = true))
                        }
                        SchedulePhase.FIRST_REVISION -> {
                            dao.updateUnit(unit.copy(revision1Completed = true))
                        }
                        SchedulePhase.SECOND_REVISION -> {
                            dao.updateUnit(unit.copy(revision2Completed = true))
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    suspend fun updateTask(task: ScheduleTaskEntity) {
        dao.updateTask(task)
    }

    suspend fun rescheduleTask(taskId: String, newDateStr: String, newTimeSlot: String) {
        dao.rescheduleTask(taskId, newDateStr, newTimeSlot)
    }

    suspend fun insertTask(task: ScheduleTaskEntity) {
        dao.insertTask(task)
    }

    suspend fun deleteTask(task: ScheduleTaskEntity) {
        dao.deleteTask(task)
    }

    suspend fun updateUnit(unit: StudyUnitEntity) {
        dao.updateUnit(unit)
    }

    suspend fun insertUnit(unit: StudyUnitEntity) {
        dao.insertUnit(unit)
    }

    suspend fun deleteUnit(unit: StudyUnitEntity) {
        dao.deleteUnit(unit)
    }

    suspend fun toggleUnitCompletion(unit: StudyUnitEntity) {
        dao.updateUnit(unit.copy(isCompleted = !unit.isCompleted))
    }

    suspend fun toggleRevision1(unit: StudyUnitEntity) {
        dao.updateUnit(unit.copy(revision1Completed = !unit.revision1Completed))
    }

    suspend fun toggleRevision2(unit: StudyUnitEntity) {
        dao.updateUnit(unit.copy(revision2Completed = !unit.revision2Completed))
    }

    suspend fun toggleWeakArea(unit: StudyUnitEntity) {
        dao.updateUnit(unit.copy(isWeakArea = !unit.isWeakArea))
    }

    // ALARMS
    suspend fun toggleAlarm(alarm: AlarmItemEntity, isEnabled: Boolean) {
        val updated = alarm.copy(isEnabled = isEnabled)
        dao.updateAlarm(updated)
        if (isEnabled) {
            alarmScheduler.scheduleAlarm(updated)
        } else {
            alarmScheduler.cancelAlarm(alarm.id)
        }
    }

    suspend fun saveAlarm(alarm: AlarmItemEntity) {
        if (alarm.id == 0) {
            val newId = dao.insertAlarm(alarm).toInt()
            val saved = alarm.copy(id = newId)
            if (saved.isEnabled) alarmScheduler.scheduleAlarm(saved)
        } else {
            dao.updateAlarm(alarm)
            if (alarm.isEnabled) {
                alarmScheduler.scheduleAlarm(alarm)
            } else {
                alarmScheduler.cancelAlarm(alarm.id)
            }
        }
    }

    suspend fun deleteAlarm(alarm: AlarmItemEntity) {
        alarmScheduler.cancelAlarm(alarm.id)
        dao.deleteAlarm(alarm)
    }

    suspend fun updateUserProfile(profile: UserProfileEntity) {
        dao.updateUserProfile(profile)
    }

    suspend fun resetAllProgress() {
        dao.resetAllTaskCompletion()
        dao.resetAllUnitProgress()
        dao.clearAllStudyLogs()
    }

    suspend fun exportDataJson(): String {
        val json = JSONObject()
        json.put("app", "STUDY_COMMAND_CENTER")
        json.put("version", "1.0")
        json.put("exportedAt", System.currentTimeMillis())
        return json.toString(2)
    }
}
