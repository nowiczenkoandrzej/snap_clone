package com.an.feature_stickers.presentation

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset


data class StickerState(
    val stickersMap: Map<String, List<String>> = emptyMap(),
    val selectedCategoryIndex: Int = 1,
    val categories: List<String> = listOf(
        "Activities",
        "Animals",
        "Clothing",
        "Emojis",
        "Food",
        "Music",
        "Objects"
    )
)

data class CuttingState(
    val currentPath: List<Offset> = emptyList(),
    val resultBitmap: Bitmap? = null
)
