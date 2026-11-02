package com.example.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.model.AlarmItemEntity
import com.example.receiver.AlarmReceiver
import java.util.Calendar

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun scheduleAlarm(alarm: AlarmItemEntity) {
        if (!alarm.isEnabled || alarmManager == null) return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            // If time is already passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_ALARM_ID, alarm.id)
            putExtra(AlarmReceiver.EXTRA_ALARM_TITLE, alarm.title)
            putExtra(AlarmReceiver.EXTRA_ALARM_TIME, alarm.timeStr)
            putExtra(AlarmReceiver.EXTRA_ALARM_SOUND, alarm.soundName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarm.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
            Log.d("AlarmScheduler", "Scheduled alarm ${alarm.id} for ${calendar.time}")
        } catch (e: SecurityException) {
            Log.e("AlarmScheduler", "Failed to schedule exact alarm: ${e.message}")
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }

    fun snoozeAlarm(alarmId: Int, title: String, snoozeMinutes: Int = 10) {
        if (alarmManager == null) return
        val calendar = Calendar.getInstance().apply {
            add(Calendar.MINUTE, snoozeMinutes)
        }

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_ALARM_ID, alarmId)
            putExtra(AlarmReceiver.EXTRA_ALARM_TITLE, "$title (Snoozed)")
            putExtra(AlarmReceiver.EXTRA_ALARM_TIME, "Snoozed +${snoozeMinutes}m")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1000 + alarmId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: Exception) {
            Log.e("AlarmScheduler", "Failed to snooze alarm: ${e.message}")
        }
    }

    fun cancelAlarm(alarmId: Int) {
        if (alarmManager == null) return
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alarmId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    fun scheduleAll(alarms: List<AlarmItemEntity>) {
        alarms.forEach { alarm ->
            if (alarm.isEnabled) {
                scheduleAlarm(alarm)
            } else {
                cancelAlarm(alarm.id)
            }
        }
    }

    companion object {
        fun getNextUpcomingAlarm(alarms: List<AlarmItemEntity>): AlarmItemEntity? {
            val enabledAlarms = alarms.filter { it.isEnabled }
            if (enabledAlarms.isEmpty()) return null

            val now = Calendar.getInstance()
            val currentHour = now.get(Calendar.HOUR_OF_DAY)
            val currentMinute = now.get(Calendar.MINUTE)
            val currentMinutesSinceMidnight = currentHour * 60 + currentMinute

            // First find alarms later today
            val todayUpcoming = enabledAlarms
                .filter { (it.hour * 60 + it.minute) > currentMinutesSinceMidnight }
                .minByOrNull { it.hour * 60 + it.minute }

            if (todayUpcoming != null) return todayUpcoming

            // Otherwise, earliest alarm tomorrow
            return enabledAlarms.minByOrNull { it.hour * 60 + it.minute }
        }
    }
}
