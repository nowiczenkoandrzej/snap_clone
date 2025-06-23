package com.an.facefilters.canvas.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap


const val STICKER_SIZE = 120

fun loadStickerFileNames(context: Context): List<String> {
    return context.assets.list("stickers")?.toList().orEmpty()
}

fun loadPngAssetAsImageBitmap(context: Context, fileName: String): Bitmap {
    val inputStream = context.assets.open("stickers/$fileName")
    val bitmap = BitmapFactory.decodeStream(inputStream)
    return bitmap
}
