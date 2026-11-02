package com.example.data.dao

import androidx.room.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyDao {

    // --- SUBJECTS ---
    @Query("SELECT * FROM subjects ORDER BY code ASC")
    fun getAllSubjects(): Flow<List<SubjectEntity>>

    @Query("SELECT * FROM subjects WHERE id = :subjectId")
    suspend fun getSubjectById(subjectId: String): SubjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubjects(subjects: List<SubjectEntity>)

    @Update
    suspend fun updateSubject(subject: SubjectEntity)

    // --- UNITS ---
    @Query("SELECT * FROM units ORDER BY unitNumber ASC")
    fun getAllUnits(): Flow<List<StudyUnitEntity>>

    @Query("SELECT * FROM units WHERE subjectId = :subjectId ORDER BY unitNumber ASC")
    fun getUnitsForSubject(subjectId: String): Flow<List<StudyUnitEntity>>

    @Query("SELECT * FROM units WHERE id = :unitId")
    suspend fun getUnitById(unitId: String): StudyUnitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnits(units: List<StudyUnitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnit(unit: StudyUnitEntity)

    @Update
    suspend fun updateUnit(unit: StudyUnitEntity)

    @Delete
    suspend fun deleteUnit(unit: StudyUnitEntity)

    @Query("DELETE FROM units WHERE id = :unitId")
    suspend fun deleteUnitById(unitId: String)

    // --- SCHEDULE TASKS ---
    @Query("SELECT * FROM schedule_tasks ORDER BY dateStr ASC, timeSlot ASC")
    fun getAllTasks(): Flow<List<ScheduleTaskEntity>>

    @Query("SELECT * FROM schedule_tasks WHERE dateStr = :dateStr ORDER BY timeSlot ASC")
    fun getTasksForDate(dateStr: String): Flow<List<ScheduleTaskEntity>>

    @Query("SELECT * FROM schedule_tasks WHERE phase = :phase ORDER BY dateStr ASC")
    fun getTasksForPhase(phase: SchedulePhase): Flow<List<ScheduleTaskEntity>>

    @Query("SELECT * FROM schedule_tasks WHERE dateStr < :todayDateStr AND isCompleted = 0 ORDER BY dateStr ASC")
    fun getBacklogTasks(todayDateStr: String): Flow<List<ScheduleTaskEntity>>

    @Query("SELECT * FROM schedule_tasks WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<ScheduleTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<ScheduleTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: ScheduleTaskEntity)

    @Update
    suspend fun updateTask(task: ScheduleTaskEntity)

    @Delete
    suspend fun deleteTask(task: ScheduleTaskEntity)

    @Query("UPDATE schedule_tasks SET isCompleted = :completed WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: String, completed: Boolean)

    @Query("UPDATE schedule_tasks SET isCompleted = :completed, isMissed = 0 WHERE id = :taskId")
    suspend fun markTaskDone(taskId: String, completed: Boolean)

    @Query("UPDATE schedule_tasks SET dateStr = :newDateStr, timeSlot = :newTimeSlot WHERE id = :taskId")
    suspend fun rescheduleTask(taskId: String, newDateStr: String, newTimeSlot: String)

    // --- ALARMS ---
    @Query("SELECT * FROM alarms ORDER BY hour ASC, minute ASC")
    fun getAllAlarms(): Flow<List<AlarmItemEntity>>

    @Query("SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY hour ASC, minute ASC")
    suspend fun getEnabledAlarms(): List<AlarmItemEntity>

    @Query("SELECT * FROM alarms WHERE id = :alarmId")
    suspend fun getAlarmById(alarmId: Int): AlarmItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarm(alarm: AlarmItemEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarms(alarms: List<AlarmItemEntity>)

    @Update
    suspend fun updateAlarm(alarm: AlarmItemEntity)

    @Delete
    suspend fun deleteAlarm(alarm: AlarmItemEntity)

    @Query("UPDATE alarms SET isEnabled = :enabled WHERE id = :alarmId")
    suspend fun setAlarmEnabled(alarmId: Int, enabled: Boolean)

    // --- STUDY LOGS ---
    @Query("SELECT * FROM study_logs ORDER BY completedAtTimestamp DESC")
    fun getAllStudyLogs(): Flow<List<StudyLogEntity>>

    @Insert
    suspend fun insertStudyLog(log: StudyLogEntity)

    @Query("DELETE FROM study_logs")
    suspend fun clearAllStudyLogs()

    // --- USER PROFILE ---
    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getUserProfile(): Flow<UserProfileEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(profile: UserProfileEntity)

    @Update
    suspend fun updateUserProfile(profile: UserProfileEntity)

    // Reset Progress
    @Query("UPDATE schedule_tasks SET isCompleted = 0, isMissed = 0")
    suspend fun resetAllTaskCompletion()

    @Query("UPDATE units SET isCompleted = 0, revision1Completed = 0, revision2Completed = 0")
    suspend fun resetAllUnitProgress()
}
