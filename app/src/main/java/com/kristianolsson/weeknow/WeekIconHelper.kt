package com.kristianolsson.weeknow

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Icon
import java.util.Calendar

/**
 * Draws the current week number onto a Bitmap and returns it as a notification Icon.
 *
 * Android status bar icons must be white-on-transparent; the system applies tinting.
 * Text is auto-fit to fill 95% of the bitmap so it appears as large as possible.
 */
object WeekIconHelper {

    private const val ICON_SIZE_PX = 128

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
     * Builds a [Bitmap] with the week number auto-fit to fill ~95% of the icon.
     * White text on transparent background — Android tints it for the status bar.
     */
    fun buildBitmap(weekNumber: Int, fillFactor: Float = 0.70f, showBorder: Boolean = false): Bitmap {
        val bmp = Bitmap.createBitmap(ICON_SIZE_PX, ICON_SIZE_PX, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
            textSize = ICON_SIZE_PX.toFloat()   // start large, then shrink to fit
        }

        val text = weekNumber.toString()
        val bounds = Rect()
        paint.getTextBounds(text, 0, text.length, bounds)

        // Scale text to fill [fillFactor] of the icon (whichever axis is tighter)
        val targetSize = ICON_SIZE_PX * fillFactor
        val scale = targetSize / maxOf(bounds.width(), bounds.height()).toFloat()
        paint.textSize = paint.textSize * scale

        // Draw vertically centered
        val x = (ICON_SIZE_PX / 2).toFloat()
        val y = (ICON_SIZE_PX / 2).toFloat() - (paint.descent() + paint.ascent()) / 2

        canvas.drawText(text, x, y, paint)

        if (showBorder) {
            val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.WHITE
                style = Paint.Style.STROKE
                strokeWidth = ICON_SIZE_PX * 0.08f // 8% of icon bounds for stroke
            }
            val margin = strokePaint.strokeWidth / 2f
            val cornerRadius = ICON_SIZE_PX * 0.20f // Smooth rounded corners
            canvas.drawRoundRect(
                margin, margin, 
                ICON_SIZE_PX - margin, ICON_SIZE_PX - margin, 
                cornerRadius, cornerRadius, 
                strokePaint
            )
        }

        return bmp
    }

    /** Convenience: build the bitmap and wrap as an [Icon]. */
    fun buildIcon(weekNumber: Int, fillFactor: Float = 0.70f, showBorder: Boolean = false): Icon =
        Icon.createWithBitmap(buildBitmap(weekNumber, fillFactor, showBorder))
}
