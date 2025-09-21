package com.an.feature_stickers.domain.use_cases

class StickersUseCases(
    val createNewSticker: CutImage,
    val addStickerToElements: AddStickerToElements,
    val loadStickersMap: LoadStickersMap
)