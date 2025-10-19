package com.an.feature_stickers.domain.use_cases

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Result
import kotlin.math.abs

class CutImage(
    private val bitmapCache: BitmapCache,
) {

    suspend operator fun invoke(
        editedImage: DomainImageModel,
        selectedArea: List<Point>
    ): Result<Bitmap> {

        val operatedBitmap = bitmapCache.get(editedImage.imagePath)
            ?: return Result.Failure("Something went wrong 11")

        if(selectedArea.isEmpty()) return Result.Failure("Something went wrong")

        val path = Path().apply {
            moveTo(selectedArea[0].x, selectedArea[0].y)
            selectedArea.forEach {
                lineTo(it.x,it.y)
            }
            lineTo(selectedArea[0].x, selectedArea[0].y)
        }

        val cutBitmap = extractSelectedArea(
            path = path,
            originalBitmap = operatedBitmap,
            strokeWidth = 8f
        )


        return Result.Success(cutBitmap)


    }

}

private fun extractSelectedArea(
    path: Path,
    originalBitmap: Bitmap,
    strokeWidth: Float,
): Bitmap {

    val softwareBitmap = if(originalBitmap.config == Bitmap.Config.HARDWARE) {
        originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
    } else {
        originalBitmap
    }

    val mask = Bitmap.createBitmap(
        softwareBitmap.width,
        softwareBitmap.height,
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

    canvas.drawBitmap(softwareBitmap, 0f, 0f, resultPaint)

    val bounds = android.graphics.RectF()
    path.computeBounds(bounds, true)

    val left = bounds.left.coerceAtLeast(0f).toInt()
    val top = bounds.top.coerceAtLeast(0f).toInt()
    val width = (bounds.width()).coerceAtMost(mask.width - left.toFloat()).toInt()
    val height = (bounds.height()).coerceAtMost(mask.height - top.toFloat()).toInt()

    return Bitmap.createBitmap(mask, abs(left) , abs(top), abs(width), abs(height))

}