package com.an.core_editor.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
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

class ImageRendererImpl(
    private val bitmapCache: BitmapCache
): ImageRenderer {




    override fun render(model: DomainImageModel): Bitmap? {
        val base = bitmapCache.get(model.imagePath) ?: return null

        var output = Bitmap.createBitmap(
            base.width,
            base.height,
            Bitmap.Config.ARGB_8888
        )

        Log.d("TAG", "render: ")

        output =  applyFilter(output, getPhotoFilterByName(model.currentFilter))

        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)


        canvas.drawBitmap(base, 0f, 0f, paint)

        model.subjectMask?.let { mask ->
            applyMask(output, mask)
        }

        val cropped = applyCrop(output, model.viewRect)


        drawPaths(cropped, model.drawingPaths)
        drawPaths(cropped, model.cutPaths)
        drawRubber(cropped, model.rubberPaths)

        return cropped

    }

    private fun applyMask(bitmap: Bitmap, mask: BooleanArray) {
        val w = bitmap.width
        val h = bitmap.height
        if (mask.size != w * h) return

        val pixels = IntArray(w * h)
        bitmap.getPixels(pixels, 0, w, 0, 0, w, h)
        for (i in pixels.indices) {
            if (!mask[i]) {
                pixels[i] = pixels[i] and 0x00FFFFFF
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
                color = android.graphics.Color.TRANSPARENT
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
                strokeWidth = pathData.thickness
                xfermode = android.graphics.PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.CLEAR
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

}