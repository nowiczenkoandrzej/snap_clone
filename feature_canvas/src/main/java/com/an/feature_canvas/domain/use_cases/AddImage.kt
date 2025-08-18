package com.an.feature_canvas.domain.use_cases

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.graphics.scale
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.ImageData
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Result

class AddImage(
    private val editorRepository: EditorRepository,
    private val bitmapCache: BitmapCache,
    private val context: Context
) {

    suspend operator fun invoke(
        uri: Uri,
        screenWidth: Float,
        screenHeight: Float,
        padding: Float
    ): Result<Unit> {




        val originalBitmap =context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }

        if(originalBitmap == null) return Result.Failure("Couldn't find image")

        val scale = minOf(
            (screenWidth - padding) / originalBitmap!!.width,
            (screenHeight - padding) / originalBitmap!!.height
        )

        val bitmap = originalBitmap.scale(
            (originalBitmap.width * scale).toInt(),
            (originalBitmap.height * scale).toInt()
        ).copy(Bitmap.Config.ARGB_8888, true)

        bitmapCache.add(
            path = uri.toString(),
            bitmap = bitmap
        )

        val imageModel = DomainImageModel(
            rotationAngle = 0f,
            scale = 1f,
            position = Point.ZERO,
            alpha = 1f,
            image = ImageData(
                width = bitmap.width,
                path = uri.toString(),
                height = bitmap.height,
                currentFilter = "Original"
            )
        )

        editorRepository.addElement(imageModel)

        return Result.Success(Unit)
    }

}