package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey val id: String, // e.g. "FA", "BL", "MPA", "GE", "AEC", "SEC", "VAC"
    val code: String,
    val name: String,
    val colorHex: Long, // e.g. 0xFF00D2FF
    val isEditable: Boolean = false, // GE, AEC, SEC, VAC are customizable
    val totalUnitsCount: Int = 5,
    val description: String = ""
)

@Entity(tableName = "units")
data class StudyUnitEntity(
    @PrimaryKey val id: String, // e.g. "FA_U1"
    val subjectId: String,
    val unitNumber: Int,
    val title: String,
    val isCompleted: Boolean = false,
    val revision1Completed: Boolean = false,
    val revision2Completed: Boolean = false,
    val isWeakArea: Boolean = false,
    val importantTopics: String = "",
    val personalNotes: String = "",
    val startDate: String = "2026-11-03",
    val deadlineDate: String = "2026-11-24"
)

enum class SchedulePhase {
    SYLLABUS_COMPLETION,
    FIRST_REVISION,
    SECOND_REVISION,
    PRACTICE_MOCK,
    FINAL_PREPARATION
}

@Entity(tableName = "schedule_tasks")
data class ScheduleTaskEntity(
    @PrimaryKey val id: String,
    val dateStr: String, // Format: "YYYY-MM-DD" e.g. "2026-11-03"
    val timeSlot: String, // e.g. "4:00 PM – 6:00 PM", "6:30 PM – 8:30 PM"
    val subjectId: String,
    val subjectName: String,
    val unitId: String = "",
    val taskTitle: String,
    val phase: SchedulePhase,
    val isCompleted: Boolean = false,
    val isMissed: Boolean = false,
    val notes: String = "",
    val durationMinutes: Int = 120
)

@Entity(tableName = "alarms")
data class AlarmItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val timeStr: String, // "04:00 AM"
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val isDailyRoutine: Boolean = true,
    val soundName: String = "Digital Radar",
    val description: String = ""
)

@Entity(tableName = "study_logs")
data class StudyLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dateStr: String,
    val durationMinutes: Int,
    val subjectName: String,
    val taskTitle: String,
    val completedAtTimestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Int = 1,
    val studentName: String = "B.Com (Hons.) Scholar",
    val targetExamDateStr: String = "2026-12-06",
    val dailyGoalMinutes: Int = 240, // 4 hours
    val streakDays: Int = 1,
    val lastActiveDateStr: String = "2026-11-03",
    val isDarkMode: Boolean = true
)
