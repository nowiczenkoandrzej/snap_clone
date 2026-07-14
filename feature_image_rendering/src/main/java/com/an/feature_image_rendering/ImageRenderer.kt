package com.an.feature_image_rendering

import android.graphics.Bitmap
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.DomainElement

interface ImageRenderer {
    suspend fun renderImage(
        imageSourcePath: String,
        edits: List<DomainImageEdit>
    ): Bitmap?

    suspend fun renderCollage(
        elements: List<DomainElement>
    )
}