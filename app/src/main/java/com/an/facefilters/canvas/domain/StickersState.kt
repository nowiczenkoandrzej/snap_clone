package com.an.facefilters.canvas.domain

import java.io.File

data class StickersState(
    val selectedCategory: StickerCategory = StickerCategory.EMOJIS,
    val categories: List<String> = emptyList(),
    val stickers: List<String> = emptyList(),
    val userStickers: List<File> = emptyList()
)
