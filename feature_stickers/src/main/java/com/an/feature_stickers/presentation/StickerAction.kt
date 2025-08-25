package com.an.feature_stickers.presentation

import android.graphics.Bitmap
import com.an.core_editor.domain.model.Point
import com.an.feature_stickers.domain.StickerCategory

sealed interface StickerAction{
    data class LoadStickersByCategory(val category: StickerCategory, val index: Int): StickerAction
    data class CreateSticker(val imagePath: String, val selectedArea: List<Point>): StickerAction
    data class AddSticker(val stickerPath: String): StickerAction
}
