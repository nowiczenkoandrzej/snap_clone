package com.an.feature_stickers.presentation

import com.an.feature_stickers.domain.StickerCategory

data class StickerState(
    val stickers: List<String> = emptyList(),
    val next: List<String> = emptyList(),
    val prev: List<String> = emptyList(),
    val userStickers: List<String> = emptyList(),
    val selectedCategoryIndex: Int = 1,
    val categories: List<String> = listOf(
        "Yours",
        "Activities",
        "Animals",
        "Clothing",
        "Emojis",
        "Food",
        "Music",
        "Objects"
    ),
)
