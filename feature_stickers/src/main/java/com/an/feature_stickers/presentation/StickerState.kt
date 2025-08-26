package com.an.feature_stickers.presentation


data class StickerState(
    val stickersMap: Map<String, List<String>> = emptyMap(),
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
    )
)
