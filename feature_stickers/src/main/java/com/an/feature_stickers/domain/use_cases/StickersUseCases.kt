package com.an.feature_stickers.domain.use_cases

class StickersUseCases(
    val createNewSticker: CreateNewSticker,
    val loadUserStickers: LoadUserStickers,
    val loadStickerByCategory: LoadStickersByCategory,
    val loadStickerCategories: LoadStickerCategories,
    val addStickerToElements: AddStickerToElements,
    val loadStickersMap: LoadStickersMap
)