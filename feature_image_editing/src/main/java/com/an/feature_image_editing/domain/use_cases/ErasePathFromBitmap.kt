package com.an.feature_image_editing.domain.use_cases

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import androidx.compose.ui.geometry.Offset
import com.an.core_editor.domain.model.Result

class ErasePathFromBitmap {

    operator fun invoke(
        path: List<Offset>,
        thickness: Float,
        bitmap: Bitmap
    ): Result<Bitmap> {
        val mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutable)
        val paint = Paint().apply {
            isAntiAlias = true
            color = android.graphics.Color.TRANSPARENT
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            strokeJoin = Paint.Join.ROUND
            strokeWidth = thickness
            xfermode = android.graphics.PorterDuffXfermode(
                android.graphics.PorterDuff.Mode.CLEAR
            )
        }
        val androidPath = Path().apply {
            if (path.isNotEmpty()) {
                moveTo(path[0].x, path[0].y)
                for (p in path) lineTo(p.x, p.y)
            }
        }
        canvas.drawPath(androidPath, paint)
        return Result.Success(mutable)
    }

}