package com.kristianolsson.weeknow

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var weekLabel: TextView
    private lateinit var enableSwitch: Switch
    private lateinit var borderSwitch: Switch
    private lateinit var firstDayToggle: MaterialButtonToggleGroup
    private lateinit var iconSizeToggle: MaterialButtonToggleGroup

    // Permission launcher for POST_NOTIFICATIONS (Android 13+)
    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                startWeekService()
            } else {
                // User denied — revert the switch without triggering our listener
                enableSwitch.setOnCheckedChangeListener(null)
                enableSwitch.isChecked = false
                PrefsHelper.setServiceEnabled(this, false)
                enableSwitch.setOnCheckedChangeListener(switchListener)
            }
        }

    private val switchListener = { _: android.widget.CompoundButton, isChecked: Boolean ->
        PrefsHelper.setServiceEnabled(this, isChecked)
        if (isChecked) {
            requestNotificationPermissionAndStart()
        } else {
            stopService(Intent(this, WeekService::class.java))
        }
        Unit
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weekLabel = findViewById(R.id.textWeekNumber)
        enableSwitch = findViewById(R.id.switchEnable)
        borderSwitch = findViewById(R.id.switchBorder)
        firstDayToggle = findViewById(R.id.toggleFirstDay)
        iconSizeToggle = findViewById(R.id.toggleIconSize)

        // Show the current week number using saved preference
        refreshWeekLabel()

        // Restore service toggle state
        enableSwitch.isChecked = PrefsHelper.isServiceEnabled(this)
        enableSwitch.setOnCheckedChangeListener(switchListener)

        // Restore border toggle state
        borderSwitch.isChecked = PrefsHelper.isShowBorderEnabled(this)
        borderSwitch.setOnCheckedChangeListener { _, isChecked ->
            PrefsHelper.setShowBorderEnabled(this, isChecked)
            if (PrefsHelper.isServiceEnabled(this)) restartWeekService()
        }

        // Restore first-day-of-week toggle state
        val savedFirstDay = PrefsHelper.getFirstDayOfWeek(this)
        firstDayToggle.check(
            if (savedFirstDay == Calendar.MONDAY) R.id.btnMonday else R.id.btnSunday
        )

        // React to first-day-of-week changes
        firstDayToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            val firstDay = if (checkedId == R.id.btnMonday) Calendar.MONDAY else Calendar.SUNDAY
            PrefsHelper.setFirstDayOfWeek(this, firstDay)
            refreshWeekLabel()
            // Restart the service so the notification icon updates immediately
            if (PrefsHelper.isServiceEnabled(this)) {
                restartWeekService()
            }
        }

        // Restore icon size toggle state
        val savedFill = PrefsHelper.getIconSizeFill(this)
        iconSizeToggle.check(
            when {
                savedFill <= 0.60f -> R.id.btnSizeSmall
                savedFill >= 0.80f -> R.id.btnSizeLarge
                else -> R.id.btnSizeMedium
            }
        )
        iconSizeToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener
            val fill = when (checkedId) {
                R.id.btnSizeSmall -> 0.55f
                R.id.btnSizeLarge -> 0.85f
                else -> 0.70f
            }
            PrefsHelper.setIconSizeFill(this, fill)
            if (PrefsHelper.isServiceEnabled(this)) restartWeekService()
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private fun refreshWeekLabel() {
        val firstDay = PrefsHelper.getFirstDayOfWeek(this)
        weekLabel.text = WeekIconHelper.currentWeekNumber(firstDay).toString()
    }

    private fun requestNotificationPermissionAndStart() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        startWeekService()
    }

    private fun startWeekService() {
        val intent = Intent(this, WeekService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    private fun restartWeekService() {
        stopService(Intent(this, WeekService::class.java))
        startWeekService()
    }
}
