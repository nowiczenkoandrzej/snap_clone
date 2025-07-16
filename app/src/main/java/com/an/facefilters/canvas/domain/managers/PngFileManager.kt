package com.an.facefilters.canvas.domain.managers

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.text.TextMeasurer
import com.an.facefilters.canvas.domain.model.Element
import java.io.File

interface PngFileManager {

    suspend fun saveSticker(bitmap: Bitmap)
    suspend fun loadUserStickers(): List<File>
    suspend fun saveAsPng(
        elements: List<Element>,
        textMeasurer: TextMeasurer,
    ): Uri?

}