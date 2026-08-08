package com.an.feature_image_rendering

import android.graphics.Bitmap
import androidx.compose.ui.text.TextMeasurer
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.presentation.mappers.toBitmapEdit
import com.an.feature_image_caching.BitmapCache
import com.an.feature_image_caching.BitmapSaver

class ImageRendererImpl(
    private val bitmapSaver: BitmapSaver,
    private val bitmapCache: BitmapCache
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

    override suspend fun renderImage(
        originalBitmap: Bitmap,
        edits: List<DomainImageEdit>,
    ): Bitmap? {

        var result = originalBitmap

        edits.forEach { edit ->
            edit.toBitmapEdit().apply(result)
        }

        return result

    }

    override suspend fun renderAndCache(elements: List<DomainImageModel>) {

        for(element in elements) {
            var originalBitmap = bitmapSaver
                .loadBitmap(element.imagePath)
                ?: continue

            val renderedBitmap = renderImage(
                originalBitmap = originalBitmap,
                edits = element.edits
            )

            bitmapCache.addOriginalAndEditedToCache(
                path = element.imagePath,
                originalBitmap = originalBitmap,
                editedBitmap = renderedBitmap ?: originalBitmap
            )

        }
    }

    override suspend fun renderCollage(
        elements: List<DomainElement>,
        width: Int,
        height: Int,
        textMeasurer: TextMeasurer
    ) {

    }

    override suspend fun renderThumbnail(
        elements: List<DomainElement>,
        width: Int,
        height: Int,
        textMeasurer: TextMeasurer
    ) {



    }

}