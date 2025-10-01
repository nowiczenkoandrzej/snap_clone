package com.an.feature_stickers.presentation

import androidx.compose.ui.geometry.Offset

sealed interface StickerAction{
    object CutBitmap: StickerAction
    object ConfirmCutting: StickerAction
    data class AddSticker(val stickerPath: String): StickerAction
    data class SelectCategory(val index: Int): StickerAction
    data class UpdateCurrentPath(val offset: Offset): StickerAction
    object AddPath: StickerAction
}
