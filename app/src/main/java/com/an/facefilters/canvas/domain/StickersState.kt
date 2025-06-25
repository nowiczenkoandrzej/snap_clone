package com.an.facefilters.canvas.domain

data class StickersState(
    val selectedCategory: StickerCategory = StickerCategory.EMOJIS,
    val categories: List<String> = emptyList(),
    val stickers: List<String> = emptyList()
)
