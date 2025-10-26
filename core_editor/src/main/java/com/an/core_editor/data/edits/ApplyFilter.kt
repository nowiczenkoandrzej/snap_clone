package com.an.core_editor.data.edits

import android.graphics.Bitmap
import com.an.core_editor.presentation.getPhotoFilterByName

data class ApplyFilter(val filterName: String): ImageEdit {
    override fun apply(input: Bitmap): Bitmap {
        val filter = getPhotoFilterByName(filterName)
        return filter.apply(input)
    }
}
