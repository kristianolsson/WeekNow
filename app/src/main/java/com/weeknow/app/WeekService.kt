package com.weeknow.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioAttributes
import android.os.IBinder

/**
 * Foreground service that posts a persistent notification displaying the current week number.
 *
 * The notification's small icon is a programmatically generated bitmap so the week
 * number appears directly in the Android status bar.
 *
 * An internal BroadcastReceiver listens for ACTION_DATE_CHANGED so the icon updates
 * automatically whenever the day (or week) rolls over at midnight.
 */
class WeekService : Service() {

    companion object {
        private const val CHANNEL_ID = "weeknow_v2"  // v2: changed from IMPORTANCE_LOW to DEFAULT
        private const val NOTIFICATION_ID = 1001
    }

    private val dateChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_DATE_CHANGED) {
                updateNotification()
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        val filter = IntentFilter(Intent.ACTION_DATE_CHANGED)
        registerReceiver(dateChangeReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, buildNotification())
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(dateChangeReceiver)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // -------------------------------------------------------------------------
    // Private helpers
    // -------------------------------------------------------------------------

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Week Number",
            // IMPORTANCE_DEFAULT is required for the icon to appear in the status bar on
            // Android 12+. IMPORTANCE_LOW suppresses it. We silence sound/vibration manually.
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Displays the current week number in the status bar"
            setShowBadge(false)
            // Silence all alerts — we only want the status bar icon, no sounds or vibration
            setSound(null, AudioAttributes.Builder().build())
            enableVibration(false)
            enableLights(false)
        }
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val firstDay = PrefsHelper.getFirstDayOfWeek(this)
        val weekNumber = WeekIconHelper.currentWeekNumber(firstDay)
        val fillFactor = PrefsHelper.getIconSizeFill(this)
        val showBorder = PrefsHelper.isShowBorderEnabled(this)
        val icon = WeekIconHelper.buildIcon(weekNumber, fillFactor, showBorder)

        // Tap on notification → open MainActivity
        val openAppIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, openAppIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return Notification.Builder(this, CHANNEL_ID)
            .setSmallIcon(icon)
            .setContentTitle("Week $weekNumber")
            .setContentIntent(pendingIntent)
            .setOngoing(true)           // Makes it undismissable by the user
            .setOnlyAlertOnce(true)
            .build()
    }

    private fun updateNotification() {
        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(NOTIFICATION_ID, buildNotification())
    }
}
