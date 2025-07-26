package com.an.facefilters.canvas.presentation.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.graphics.toArgb
import com.an.facefilters.canvas.domain.model.PathData

fun Bitmap.drawPaths(
    paths: List<PathData>,
): Bitmap {

    if(paths.isEmpty()) return this

    val resultBitmap = this.copy(Bitmap.Config.ARGB_8888, true)

    val canvas = Canvas(resultBitmap)

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

        val androidPath = Path().apply {
            val firstPoint = pathData.path.first()
            moveTo(firstPoint.x, firstPoint.y)
            pathData.path.drop(1).forEach { point ->
                lineTo(point.x, point.y)
            }
        }

        canvas.drawPath(androidPath, paint)
    }
    return resultBitmap
}