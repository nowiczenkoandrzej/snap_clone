package com.an.feature_stickers.presentation

sealed interface StickerAction{
    data class AddSticker(val stickerPath: String): StickerAction
    data class SelectCategory(val index: Int): StickerAction
}
