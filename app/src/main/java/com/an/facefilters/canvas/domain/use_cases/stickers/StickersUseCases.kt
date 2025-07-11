package com.an.facefilters.canvas.domain.use_cases.stickers

class StickersUseCases(
    val createNewSticker: CreateNewSticker,
    val loadSticker: LoadSticker,
    val loadStickerByCategory: LoadStickerByCategory,
    val loadStickerState: LoadStickerState
)
