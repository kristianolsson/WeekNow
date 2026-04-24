package com.kristianolsson.weeknow.wear

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

/**
 * Watch-side SharedPreferences for complication settings.
 * Independent from the phone app's PrefsHelper.
 */
object WearPrefsHelper {
    private const val PREFS_NAME = "weeknow_wear_prefs"
    private const val KEY_FIRST_DAY_OF_WEEK = "first_day_of_week"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Returns [Calendar.MONDAY] or [Calendar.SUNDAY]. Defaults to Monday (ISO 8601). */
    fun getFirstDayOfWeek(context: Context): Int =
        prefs(context).getInt(KEY_FIRST_DAY_OF_WEEK, Calendar.MONDAY)

    fun setFirstDayOfWeek(context: Context, firstDay: Int) {
        prefs(context).edit().putInt(KEY_FIRST_DAY_OF_WEEK, firstDay).apply()
    }
}
