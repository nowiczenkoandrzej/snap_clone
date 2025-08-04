package com.an.facefilters.canvas.domain.managers

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.text.TextMeasurer
import com.an.facefilters.canvas.domain.model.Element
import java.io.File

interface PngFileManager {

    suspend fun saveSticker(bitmap: Bitmap)
    suspend fun loadUserStickers(): List<File>
    suspend fun saveElementsAsBitmap(
        width: Int,
        height: Int,
        elements: List<Element>,
    ): Bitmap

    fun saveBitmapToGalleryAsFile(
        bitmap: Bitmap,
    ): File?

}