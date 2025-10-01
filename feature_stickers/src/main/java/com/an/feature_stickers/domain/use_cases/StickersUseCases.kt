package com.an.feature_stickers.domain.use_cases

class StickersUseCases(
    val cutImage: CutImage,
    val saveCutting: SaveCutting,
    val addStickerToElements: AddStickerToElements,
    val loadStickersMap: LoadStickersMap
)