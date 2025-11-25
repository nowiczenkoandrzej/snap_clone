package com.an.core_editor.domain

import android.graphics.Bitmap
import com.an.core_editor.domain.model.DomainImageModel

interface ImageRenderer {
    fun render(model: DomainImageModel): Bitmap?
}