package com.kristianolsson.weeknow.wear

import java.util.Calendar

/**
 * Pure week-number calculation. Duplicated from the phone module to avoid
 * a shared-library dependency for ~5 lines of code.
 */
object WeekHelper {

    /**
     * Returns the current week number using the given [firstDayOfWeek].
     *  - [Calendar.MONDAY] → ISO 8601 (European, minDays=4)
     *  - [Calendar.SUNDAY] → US/North-American convention (minDays=1)
     */
    fun currentWeekNumber(firstDayOfWeek: Int = Calendar.MONDAY): Int {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = firstDayOfWeek
        cal.minimalDaysInFirstWeek = if (firstDayOfWeek == Calendar.MONDAY) 4 else 1
        return cal.get(Calendar.WEEK_OF_YEAR)
    }

    /**
     * Returns the total number of weeks in the current year for the given convention.
     * Used by RANGED_VALUE complication to show week N of M.
     */
    fun totalWeeksInYear(firstDayOfWeek: Int = Calendar.MONDAY): Int {
        val cal = Calendar.getInstance()
        cal.firstDayOfWeek = firstDayOfWeek
        cal.minimalDaysInFirstWeek = if (firstDayOfWeek == Calendar.MONDAY) 4 else 1
        return cal.getActualMaximum(Calendar.WEEK_OF_YEAR)
    }
}
