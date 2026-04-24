package com.kristianolsson.weeknow.wear

import android.graphics.drawable.Icon
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import androidx.wear.watchface.complications.data.MonochromaticImage
import androidx.wear.watchface.complications.data.PlainComplicationText
import androidx.wear.watchface.complications.data.RangedValueComplicationData
import androidx.wear.watchface.complications.data.ShortTextComplicationData
import androidx.wear.watchface.complications.datasource.ComplicationRequest
import androidx.wear.watchface.complications.datasource.SuspendingComplicationDataSourceService

/**
 * Complication data source that provides the current ISO week number to watch faces.
 *
 * Supports:
 *  - SHORT_TEXT   → calendar icon + "17" (like the built-in calendar complication)
 *  - RANGED_VALUE → week 17 of 52 as a progress arc with "17" label
 */
class WeekComplicationService : SuspendingComplicationDataSourceService() {

    private fun weekIcon(): MonochromaticImage =
        MonochromaticImage.Builder(
            Icon.createWithResource(this, R.drawable.ic_complication_week)
        ).build()

    override fun getPreviewData(type: ComplicationType): ComplicationData? {
        return when (type) {
            ComplicationType.SHORT_TEXT -> ShortTextComplicationData.Builder(
                text = PlainComplicationText.Builder("42").build(),
                contentDescription = PlainComplicationText.Builder("Week 42").build()
            )
                .setMonochromaticImage(weekIcon())
                .build()

            ComplicationType.RANGED_VALUE -> RangedValueComplicationData.Builder(
                value = 42f,
                min = 1f,
                max = 52f,
                contentDescription = PlainComplicationText.Builder("Week 42 of 52").build()
            )
                .setText(PlainComplicationText.Builder("42").build())
                .setMonochromaticImage(weekIcon())
                .build()

            else -> null
        }
    }

    override suspend fun onComplicationRequest(request: ComplicationRequest): ComplicationData? {
        val firstDay = WearPrefsHelper.getFirstDayOfWeek(this)
        val weekNumber = WeekHelper.currentWeekNumber(firstDay)
        val totalWeeks = WeekHelper.totalWeeksInYear(firstDay)

        return when (request.complicationType) {
            ComplicationType.SHORT_TEXT -> ShortTextComplicationData.Builder(
                text = PlainComplicationText.Builder("$weekNumber").build(),
                contentDescription = PlainComplicationText.Builder("Week $weekNumber").build()
            )
                .setMonochromaticImage(weekIcon())
                .build()

            ComplicationType.RANGED_VALUE -> RangedValueComplicationData.Builder(
                value = weekNumber.toFloat(),
                min = 1f,
                max = totalWeeks.toFloat(),
                contentDescription = PlainComplicationText
                    .Builder("Week $weekNumber of $totalWeeks").build()
            )
                .setText(PlainComplicationText.Builder("$weekNumber").build())
                .setMonochromaticImage(weekIcon())
                .build()

            else -> null
        }
    }
}
