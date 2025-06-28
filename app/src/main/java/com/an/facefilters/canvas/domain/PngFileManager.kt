package com.an.facefilters.canvas.domain

import android.graphics.Bitmap
import java.io.File

interface PngFileManager {

    fun saveSticker(bitmap: Bitmap)
    fun loadUserStickers(): List<File>

}