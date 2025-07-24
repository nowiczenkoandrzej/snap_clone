package com.an.facefilters.canvas.presentation.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.graphics.toArgb
import com.an.facefilters.canvas.domain.model.PathData

fun Bitmap.drawPaths(paths: List<PathData>): Bitmap {

    if(paths.isEmpty()) return this

    val resultBitmap = this.copy(Bitmap.Config.ARGB_8888, true)

    val canvas = Canvas(resultBitmap)

    for (pathData in paths) {
        val paint = Paint().apply {
            color = pathData.color.toArgb()
            strokeWidth = pathData.thickness
            style = Paint.Style.STROKE
            isAntiAlias = true
        }

        canvas.drawPath(pathData.path, paint)
    }
}