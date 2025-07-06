package com.an.facefilters.canvas.domain

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.text.TextMeasurer
import com.an.facefilters.canvas.domain.model.Element
import java.io.File

interface PngFileManager {

    fun saveSticker(bitmap: Bitmap)
    fun loadUserStickers(): List<File>
    fun saveAsPng(
        elements: List<Element>,
        textMeasurer: TextMeasurer,
    ): Uri?

}