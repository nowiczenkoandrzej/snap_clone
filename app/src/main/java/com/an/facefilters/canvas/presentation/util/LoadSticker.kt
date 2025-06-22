package com.an.facefilters.canvas.presentation.util

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap

fun loadStickerFileNames(context: Context): List<String> {
    return context.assets.list("stickers")?.toList().orEmpty()
}

fun String.toImageBitmap(
    context: Context,
): ImageBitmap? {
    try {
        val inputStream = context.assets.open("stickers/$this")
        val svg = SVG
    }
}