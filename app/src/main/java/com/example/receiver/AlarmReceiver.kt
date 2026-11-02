package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import com.example.alarm.AlarmScheduler
import com.example.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "study_command_alarms_channel"
        const val CHANNEL_NAME = "Study Command Alarms & Routines"
        const val EXTRA_ALARM_ID = "extra_alarm_id"
        const val EXTRA_ALARM_TITLE = "extra_alarm_title"
        const val EXTRA_ALARM_TIME = "extra_alarm_time"
        const val EXTRA_ALARM_SOUND = "extra_alarm_sound"
        const val ACTION_SNOOZE = "com.example.ACTION_SNOOZE"
        const val ACTION_DISMISS = "com.example.ACTION_DISMISS"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        val alarmId = intent.getIntExtra(EXTRA_ALARM_ID, 1)
        val title = intent.getStringExtra(EXTRA_ALARM_TITLE) ?: "Study Session Alert"
        val timeStr = intent.getStringExtra(EXTRA_ALARM_TIME) ?: ""

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (action == ACTION_SNOOZE) {
            notificationManager.cancel(alarmId)
            AlarmScheduler(context).snoozeAlarm(alarmId, title, 10)
            return
        }

        if (action == ACTION_DISMISS) {
            notificationManager.cancel(alarmId)
            return
        }

        createNotificationChannel(context, notificationManager)
        showNotification(context, notificationManager, alarmId, title, timeStr)
        playFeedback(context)

        // Reschedule recurring alarm for next day
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(context, this)
            val alarm = db.studyDao().getAlarmById(alarmId)
            if (alarm != null && alarm.isEnabled && alarm.isDailyRoutine) {
                AlarmScheduler(context).scheduleAlarm(alarm)
            }
        }
    }

    private fun createNotificationChannel(
        context: Context,
        notificationManager: NotificationManager
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val defaultSoundUri: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications and loud alerts for scheduled study sessions and exams"
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500, 250, 800)
                setSound(defaultSoundUri, audioAttributes)
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(
        context: Context,
        notificationManager: NotificationManager,
        alarmId: Int,
        title: String,
        timeStr: String
    ) {
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val openAppPendingIntent = PendingIntent.getActivity(
            context,
            alarmId,
            openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Snooze intent
        val snoozeIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_ALARM_ID, alarmId)
            putExtra(EXTRA_ALARM_TITLE, title)
        }
        val snoozePendingIntent = PendingIntent.getBroadcast(
            context,
            2000 + alarmId,
            snoozeIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Dismiss intent
        val dismissIntent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_DISMISS
            putExtra(EXTRA_ALARM_ID, alarmId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            3000 + alarmId,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("STUDY COMMAND CENTER")
            .setContentText("$title • $timeStr")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Time for: $title ($timeStr)\nStay disciplined. Your future is built by what you study today!"))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setVibrate(longArrayOf(0, 500, 250, 500, 250, 800))
            .setContentIntent(openAppPendingIntent)
            .addAction(android.R.drawable.ic_popup_sync, "Snooze 10m", snoozePendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", dismissPendingIntent)
            .build()

        notificationManager.notify(alarmId, notification)
    }

    private fun playFeedback(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    android.os.VibrationEffect.createWaveform(
                        longArrayOf(0, 400, 200, 400),
                        -1
                    )
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(longArrayOf(0, 400, 200, 400), -1)
            }
        } catch (_: Exception) {}

        try {
            val alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(context, alert)
            ringtone?.play()
        } catch (_: Exception) {}
    }
}
