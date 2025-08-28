package com.an.feature_stickers.presentation.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.createBitmap

fun extractSelectedArea(
    path: Path,
    originalBitmap: Bitmap,
    strokeWidth: Float,
): Bitmap {

    val softwareBitmap = if(originalBitmap.config == Bitmap.Config.HARDWARE) {
        originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
    } else {
        originalBitmap
    }



    val output = createBitmap(softwareBitmap.width, softwareBitmap.height)

    val canvas = Canvas(output)

    val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
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
    return output

}