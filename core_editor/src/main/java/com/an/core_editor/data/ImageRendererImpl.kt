package com.an.core_editor.data

import android.graphics.Bitmap

import com.an.core_editor.domain.ImageRenderer
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.presentation.mappers.toBitmapEdit


class ImageRendererImpl(
    private val bitmapCache: BitmapCache
): ImageRenderer {


    override fun render(model: DomainImageModel): Bitmap? {
        val base = bitmapCache.get(model.imagePath) ?: return null

        var output = base.copy(Bitmap.Config.ARGB_8888, true)
        model.edits.forEach { edit ->
            output = edit.toBitmapEdit().apply(output)
        }

        return output

    }

}