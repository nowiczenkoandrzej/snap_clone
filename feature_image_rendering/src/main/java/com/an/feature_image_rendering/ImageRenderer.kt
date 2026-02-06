package com.an.feature_image_rendering

import android.graphics.Bitmap
import com.an.core_editor.domain.model.DomainElement

interface ImageRenderer {
    suspend fun render(elements: List<DomainElement>): Bitmap?
}