package com.an.core_editor.data.edits

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import androidx.core.graphics.createBitmap
import com.an.core_editor.domain.model.PathData
import kotlin.math.abs

class CutImageBitmapEdit(
    val path: PathData
): BitmapEdit {
    override fun apply(input: Bitmap): Bitmap {
        path.let { cut ->

            val path = Path().apply {
                moveTo(cut.path[0].x, cut.path[0].y)
                cut.path.forEach { lineTo(it.x, it.y) }
                lineTo(cut.path[0].x, cut.path[0].y)
            }
            val mask = createBitmap(input.width, input.height)

            val canvas = Canvas(mask)

            val paint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.FILL
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
                this.strokeWidth = strokeWidth
                color = Color.BLACK
            }

            canvas.drawPath(path, paint)

            val resultPaint = Paint().apply {
                xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            }
            canvas.drawBitmap(input, 0f, 0f, resultPaint)

            val bounds = RectF()
            path.computeBounds(bounds, true)

            val left = bounds.left.coerceAtLeast(0f).toInt()
            val top = bounds.top.coerceAtLeast(0f).toInt()
            val width = (bounds.width()).coerceAtMost(mask.width - left.toFloat()).toInt()
            val height = (bounds.height()).coerceAtMost(mask.height - top.toFloat()).toInt()

            return Bitmap.createBitmap(mask, abs(left), abs(top), abs(width), abs(height))

        }
    }
}