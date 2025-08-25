package com.an.feature_stickers.presentation

sealed interface StickerEvent {
    object PopBackStack: StickerEvent
    data class ShowSnackbar(val message: String): StickerEvent
}