package com.an.feature_image_rendering

import android.graphics.Bitmap
import androidx.compose.ui.text.TextMeasurer
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel

interface ImageRenderer {
    suspend fun renderImage(
        imageSourcePath: String,
        edits: List<DomainImageEdit>
    ): Bitmap?

    suspend fun renderImage(
        originalBitmap: Bitmap,
        edits: List<DomainImageEdit>
    ): Bitmap?

    suspend fun renderAndCache(
        elements: List<DomainImageModel>
    )

    suspend fun renderCollage(
        elements: List<DomainElement>,
        width: Int,
        height: Int,
        textMeasurer: TextMeasurer
    )
    suspend fun renderThumbnail(
        elements: List<DomainElement>,
        width: Int,
        height: Int,
        textMeasurer: TextMeasurer
    )
}