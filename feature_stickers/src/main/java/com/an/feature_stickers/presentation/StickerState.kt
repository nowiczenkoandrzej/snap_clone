package com.an.feature_stickers.presentation

import com.an.feature_stickers.domain.StickerCategory
import java.io.File

data class StickerState(
    val selectedCategory: StickerCategory = StickerCategory.ACTIVITIES,
    val categories: List<String> = emptyList(),
    val stickers: List<String> = emptyList(),
    val userStickers: List<String> = emptyList()
)
