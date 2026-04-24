package com.kristianolsson.weeknow.wear

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import java.util.Calendar

/**
 * Minimal configuration activity shown when the user adds the week-number
 * complication to a watch face. Lets them pick Monday or Sunday as the
 * first day of the week.
 *
 * The complication system requires this activity to call [setResult] with
 * [RESULT_OK] before finishing for the complication to be added.
 */
class ComplicationConfigActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complication_config)

        // Default result is CANCELED — complication won't be added if the user backs out
        setResult(RESULT_CANCELED)

        val title = findViewById<TextView>(R.id.configTitle)
        val btnMonday = findViewById<Button>(R.id.btnMonday)
        val btnSunday = findViewById<Button>(R.id.btnSunday)

        // Highlight current selection
        val current = WearPrefsHelper.getFirstDayOfWeek(this)
        updateSelection(btnMonday, btnSunday, current)

        btnMonday.setOnClickListener {
            WearPrefsHelper.setFirstDayOfWeek(this, Calendar.MONDAY)
            setResult(RESULT_OK)
            finish()
        }

        btnSunday.setOnClickListener {
            WearPrefsHelper.setFirstDayOfWeek(this, Calendar.SUNDAY)
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun updateSelection(btnMonday: Button, btnSunday: Button, firstDay: Int) {
        if (firstDay == Calendar.MONDAY) {
            btnMonday.alpha = 1.0f
            btnSunday.alpha = 0.4f
        } else {
            btnMonday.alpha = 0.4f
            btnSunday.alpha = 1.0f
        }
    }
}
