package com.an.feature_canvas.domain.use_cases

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.graphics.scale
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.model.Result

class AddImageFromSavedProject(
    private val bitmapCache: BitmapCache,
    private val context: Context
) {

    suspend operator fun invoke(
        path: String,
        screenWidth: Float,
        screenHeight: Float,
        padding: Float
    ): Result<Unit> {
        val uri = Uri.parse(path)

        val originalBitmap = context.contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it)
        }

        if(originalBitmap == null) return Result.Failure("Couldn't find image")

        Log.d("TAG", "invoke: $originalBitmap")

        val scale = minOf(
            (screenWidth - padding) / originalBitmap.width,
            (screenHeight - padding) / originalBitmap.height
        )

        val bitmap = originalBitmap.scale(
            (originalBitmap.width * scale).toInt(),
            (originalBitmap.height * scale).toInt()
        ).copy(Bitmap.Config.ARGB_8888, true)

        bitmapCache.add(
            path = path,
            bitmap = bitmap
        )

        return Result.Success(Unit)
    }

}