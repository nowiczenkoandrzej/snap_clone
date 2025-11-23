package com.an.core_editor.data.edits

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import com.an.core_editor.domain.model.PathData

class DrawRubberBitmapEdit(
    val paths: List<PathData>
): BitmapEdit{
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