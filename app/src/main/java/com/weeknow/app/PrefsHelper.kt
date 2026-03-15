package com.weeknow.app

import android.content.Context
import android.content.SharedPreferences
import java.util.Calendar

/**
 * Simple wrapper around SharedPreferences for app settings.
 */
object PrefsHelper {
    private const val PREFS_NAME = "weeknow_prefs"
    private const val KEY_SERVICE_ENABLED = "service_enabled"
    private const val KEY_FIRST_DAY_OF_WEEK = "first_day_of_week"
    private const val KEY_ICON_SIZE_FILL = "icon_size_fill"

    private fun prefs(context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isServiceEnabled(context: Context): Boolean =
        prefs(context).getBoolean(KEY_SERVICE_ENABLED, false)

    fun setServiceEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_SERVICE_ENABLED, enabled).apply()
    }

    /** Returns [Calendar.MONDAY] or [Calendar.SUNDAY]. Defaults to Monday. */
    fun getFirstDayOfWeek(context: Context): Int =
        prefs(context).getInt(KEY_FIRST_DAY_OF_WEEK, Calendar.MONDAY)

    fun setFirstDayOfWeek(context: Context, firstDay: Int) {
        prefs(context).edit().putInt(KEY_FIRST_DAY_OF_WEEK, firstDay).apply()
    }

    /**
     * Fill factor for the status bar icon text (ratio of text height to bitmap height).
     * Small=0.55, Medium=0.70, Large=0.85. Defaults to Medium.
     */
    fun getIconSizeFill(context: Context): Float =
        prefs(context).getFloat(KEY_ICON_SIZE_FILL, 0.70f)

    fun setIconSizeFill(context: Context, fill: Float) {
        prefs(context).edit().putFloat(KEY_ICON_SIZE_FILL, fill).apply()
    }
}
