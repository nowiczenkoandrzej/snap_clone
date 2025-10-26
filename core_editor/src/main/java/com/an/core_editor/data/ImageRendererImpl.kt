package com.an.core_editor.data

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
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
import kotlin.math.abs

class ImageRendererImpl(
    private val bitmapCache: BitmapCache
): ImageRenderer {


    override fun render(model: DomainImageModel): Bitmap? {
        val base = bitmapCache.get(model.imagePath) ?: return null

        var output = base.copy(Bitmap.Config.ARGB_8888, true)
        model.edits.forEach { edit ->
            output = edit.apply(output)
        }

        return output

    }

}