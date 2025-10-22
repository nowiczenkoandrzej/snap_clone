package com.an.core_editor.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.util.Log
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toArgb
import com.an.core_editor.domain.ImageRenderer
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.presentation.PhotoFilter
import com.an.core_editor.presentation.getPhotoFilterByName
import com.an.core_editor.presentation.toDomainColor
import kotlin.math.abs

class ImageRendererImpl(
    private val bitmapCache: BitmapCache
): ImageRenderer {


    override fun render(model: DomainImageModel): Bitmap? {
        val base = bitmapCache.get(model.imagePath) ?: return null

        val filtered = getPhotoFilterByName(model.currentFilter).apply(base)

        //Log.d("TAG", "imageRender: $filter")

        val output = Bitmap.createBitmap(filtered.width, filtered.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawBitmap(filtered, 0f, 0f, null)

        model.subjectMask?.let { mask ->
            applyMask(output, mask)
        }


        val cropped = applyCrop(output, model.viewRect)


        drawPaths(cropped, model.drawingPaths)
        drawRubber(cropped, model.rubberPaths)

        var result = cropped
        model.cutPaths.forEach { cut ->
            val path = Path().apply {
                moveTo(cut.path[0].x, cut.path[0].y)
                cut.path.forEach { lineTo(it.x, it.y) }
                lineTo(cut.path[0].x, cut.path[0].y)
            }
            result = extractSelectedArea(path, cropped, cut.thickness)
        }

        return result

    }

    private fun applyMask(bitmap: Bitmap, mask: BooleanArray) {
        val w = bitmap.width
        val h = bitmap.height
        if (mask.size != w * h) return

        val pixels = IntArray(w * h)
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h)
        for (i in pixels.indices) {
            if (!mask[i]) {
                pixels[i] = pixels[i] and 0x00FFFFFF // alpha = 0
            } else {
                pixels[i] = pixels[i] or (0xFF shl 24) // alpha = 255
            }
        }
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
    }

    private fun applyCrop(bitmap: Bitmap, cropRect: Rect?): Bitmap {
        cropRect ?: return bitmap
        val left = cropRect.left.toInt().coerceIn(0, bitmap.width)
        val top = cropRect.top.toInt().coerceIn(0, bitmap.height)
        val width = cropRect.width.toInt().coerceAtMost(bitmap.width - left)
        val height = cropRect.height.toInt().coerceAtMost(bitmap.height - top)

        return Bitmap.createBitmap(bitmap, left, top, width, height)
    }

    private fun drawPaths(bitmap: Bitmap, paths: List<PathData>) {
        val canvas = Canvas(bitmap)
        for (pathData in paths) {

            if (pathData.path.isEmpty()) continue

            val paint = Paint().apply {
                isAntiAlias = true
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
                this.strokeWidth = pathData.thickness
                color = pathData.color.toArgb()
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
    }

    private fun drawRubber(bitmap: Bitmap, paths: List<PathData>) {
        val canvas = Canvas(bitmap)
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
    }

    private fun applyFilter(bitmap: Bitmap, filter: PhotoFilter): Bitmap {

        return filter.apply(bitmap)

    }

    private fun extractSelectedArea(
        path: Path,
        originalBitmap: Bitmap,
        strokeWidth: Float,
    ): Bitmap {


        val mask = Bitmap.createBitmap(
            originalBitmap.width,
            originalBitmap.height,
            Bitmap.Config.ARGB_8888
        )

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

        canvas.drawBitmap(originalBitmap, 0f, 0f, resultPaint)

        val bounds = RectF()
        path.computeBounds(bounds, true)

        val left = bounds.left.coerceAtLeast(0f).toInt()
        val top = bounds.top.coerceAtLeast(0f).toInt()
        val width = (bounds.width()).coerceAtMost(mask.width - left.toFloat()).toInt()
        val height = (bounds.height()).coerceAtMost(mask.height - top.toFloat()).toInt()

        return Bitmap.createBitmap(mask, abs(left) , abs(top), abs(width), abs(height))

    }

}