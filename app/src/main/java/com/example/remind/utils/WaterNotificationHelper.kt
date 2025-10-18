package com.example.remind.utils

import android.Manifest
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.remind.R
import com.example.remind.data.repository.WaterRepository
import com.example.remind.ui.activity.MainActivity

/**
 * Helper class for managing hydration reminder notifications
 */
class WaterNotificationHelper(private val context: Context) {

    companion object {
        private const val CHANNEL_ID = "hydration_reminders"
        private const val CHANNEL_NAME = "Hydration Reminders"
        private const val NOTIFICATION_ID = 1001
        private const val REMINDER_INTERVAL = 2 * 1000L // 2 seconds for testing
        private const val REQUEST_CODE_BASE = 2000
    }

    init {
        createNotificationChannel()
    }

    /**
     * Create notification channel for Android O and above
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Reminders to drink water throughout the day"
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun scheduleHydrationReminders() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Cancel any existing reminders first
        cancelHydrationReminders()

        // Schedule reminder to start in 10 seconds, then repeat every 10 seconds
        val triggerTime = System.currentTimeMillis() + REMINDER_INTERVAL

        val intent = Intent(context, WaterReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_BASE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        //  add repeating alarms every 10 seconds
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            REMINDER_INTERVAL, // Repeat every 10 seconds
            pendingIntent
        )
    }

    fun cancelHydrationReminders() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, WaterReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE_BASE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    /**
     * show hydration reminder notification
     */
    fun showHydrationNotification() {
        // Check if we have notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "hydration")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_water)
            .setContentTitle(context.getString(R.string.time_to_drink_water))
            .setContentText(context.getString(R.string.stay_hydrated))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, notification)
        }
    }

}

class WaterReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val waterRepository = WaterRepository(context)
        
        // Only show notification if reminders are enabled
        if (waterRepository.areRemindersEnabled()) {
            val notificationHelper = WaterNotificationHelper(context)
            notificationHelper.showHydrationNotification()
        }
    }
}

