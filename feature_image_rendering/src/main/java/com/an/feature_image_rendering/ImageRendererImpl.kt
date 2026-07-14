package com.an.feature_image_rendering

import android.graphics.Bitmap
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.presentation.mappers.toBitmapEdit
import com.an.feature_image_caching.BitmapSaver

class ImageRendererImpl(
    private val bitmapSaver: BitmapSaver
): ImageRenderer {
    override suspend fun renderImage(
        imageSourcePath: String,
        edits: List<DomainImageEdit>
    ): Bitmap? {

        var output = bitmapSaver
            .loadBitmap(imageSourcePath)
            ?: return null

        edits.forEach { edit ->
            edit.toBitmapEdit().apply(output)
        }

        return output

    }

    override suspend fun renderCollage(elements: List<DomainElement>) {
        TODO("Not yet implemented")
    }

}