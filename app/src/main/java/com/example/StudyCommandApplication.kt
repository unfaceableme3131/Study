package com.example

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.alarm.AlarmScheduler
import com.example.data.AppDatabase
import com.example.receiver.AlarmReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StudyCommandApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val alarmScheduler: AlarmScheduler by lazy { AlarmScheduler(this) }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        // Ensure default data exists & schedule enabled alarms
        applicationScope.launch {
            val enabledAlarms = database.studyDao().getEnabledAlarms()
            if (enabledAlarms.isNotEmpty()) {
                alarmScheduler.scheduleAll(enabledAlarms)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                AlarmReceiver.CHANNEL_ID,
                AlarmReceiver.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Study alarms, session reminders, and deadline alerts"
                enableVibration(true)
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
