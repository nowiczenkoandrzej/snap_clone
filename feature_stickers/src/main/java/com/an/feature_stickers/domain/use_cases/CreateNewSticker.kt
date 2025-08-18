package com.an.feature_stickers.domain.use_cases

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.createBitmap
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Result
import com.an.feature_stickers.domain.StickerManager
import com.an.feature_stickers.domain.SubjectDetector
import kotlinx.coroutines.delay

class CreateNewSticker(
    private val stickerManager: StickerManager,
    private val bitmapCache: BitmapCache,
    private val subjectDetector: SubjectDetector
) {

    suspend operator fun invoke(
        imagePath: String,
        selectedArea: List<Point>
    ): Result<Bitmap> {



        val operatedBitmap = bitmapCache.getEdited(imagePath)
            ?: return Result.Failure("Something went wrong")

        if(selectedArea.isEmpty()) return Result.Failure("Something went wrong")

        val path = Path().apply {
            moveTo(selectedArea[0].x, selectedArea[0].y)
            selectedArea.forEach {
                lineTo(it.x,it.y)
            }
        }

        val cutBitmap = extractSelectedArea(
            path = path,
            originalBitmap = operatedBitmap,
            strokeWidth = 200f
        )
        var result: Result<Bitmap>? = null
        subjectDetector.detectSubject(
            bitmap = cutBitmap,
            onSubjectDetected = { bitmap ->

                result = Result.Success(bitmap)
            },
            onError = { message ->
                result = Result.Failure(message)
            }
        )

        while(result == null) {
            delay(100)
        }
        val res = (result as Result<Bitmap>)
        /*if(res is Result.Success) {
            stickerManager.createNewSticker(res.data)
        }*/

        return res

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