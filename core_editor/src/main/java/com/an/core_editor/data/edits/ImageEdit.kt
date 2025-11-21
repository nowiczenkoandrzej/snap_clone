package com.an.core_editor.data.edits

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.createBitmap
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.presentation.getPhotoFilterByName
import kotlin.math.abs

sealed interface ImageEdit {
    fun apply(input: Bitmap): Bitmap

    data class ApplyFilter(val filterName: String): ImageEdit {
        override fun apply(input: Bitmap): Bitmap {
            val filter = getPhotoFilterByName(filterName)
            return filter.apply(input)
        }
    }

    data class CropImage(val cropRect: Rect): ImageEdit {
        override fun apply(input: Bitmap): Bitmap {
            val left = cropRect.left.toInt().coerceIn(0, input.width)
            val top = cropRect.top.toInt().coerceIn(0, input.height)
            val width = cropRect.width.toInt().coerceAtMost(input.width - left)
            val height = cropRect.height.toInt().coerceAtMost(input.height - top)

            return Bitmap.createBitmap(input, left, top, width, height)
        }
    }

    data class DrawRubber(val paths: List<PathData>): ImageEdit {
        override fun apply(input: Bitmap): Bitmap {
            val output = input.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(output)

            for (pathData in paths) {

                if (pathData.path.isEmpty()) continue

                val paint = Paint().apply {
                    isAntiAlias = true
                    color = Color.TRANSPARENT
                    style = Paint.Style.STROKE
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.ROUND
                    strokeWidth = pathData.thickness
                    xfermode = PorterDuffXfermode(
                        PorterDuff.Mode.CLEAR
                    )
                }
                val androidPath = Path().apply {
                    if (pathData.path.isNotEmpty()) {
                        moveTo(pathData.path[0].x, pathData.path[0].y)
                        for (p in pathData.path) lineTo(p.x, p.y)
                    }
                }
                canvas.drawPath(androidPath, paint)
            }

            return output
        }

    }

    data class CutImage(val path: PathData): ImageEdit {
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

    data class DrawPaths(val paths: List<PathData>): ImageEdit {
        override fun apply(input: Bitmap): Bitmap {
            val output = input.copy(Bitmap.Config.ARGB_8888, true)
            val canvas = Canvas(output)


            for (pathData in paths) {

                if (pathData.path.isEmpty()) continue

                val paint = Paint().apply {
                    isAntiAlias = true
                    style = Paint.Style.STROKE
                    strokeCap = Paint.Cap.ROUND
                    strokeJoin = Paint.Join.ROUND
                    this.strokeWidth = pathData.thickness
                    color = pathData.color.toCompose().toArgb()
                }
                val path = Path().apply {
                    val firstPoint = pathData.path.first()
                    moveTo(firstPoint.x, firstPoint.y)
                    pathData.path.drop(1).forEach { point ->
                        lineTo(point.x, point.y)
                    }
                }
                canvas.drawPath(path, paint)
            }
            return output
        }

    }

    class RemoveBackground(val mask: BooleanArray): ImageEdit {
        override fun apply(input: Bitmap): Bitmap {
            val w = input.width
            val h = input.height
            if (mask.size != w * h) return input

            val pixels = IntArray(w * h)
            input.getPixels(pixels, 0, w, 0, 0, w, h)
            for (i in pixels.indices) {
                if (!mask[i]) {
                    pixels[i] = Color.TRANSPARENT
                }
            }
            input.setPixels(pixels, 0, w, 0, 0, w, h)


            return input

        }

    }
}