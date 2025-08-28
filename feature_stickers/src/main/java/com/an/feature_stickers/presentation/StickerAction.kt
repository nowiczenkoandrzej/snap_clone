package com.an.feature_stickers.presentation

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Offset
import com.an.core_editor.domain.model.Point
import com.an.feature_stickers.domain.StickerCategory

sealed interface StickerAction{
    data class CreateSticker(val imagePath: String, val selectedArea: List<Point>): StickerAction
    data class AddSticker(val stickerPath: String, val isFromAssets: Boolean): StickerAction
    data class SelectCategory(val index: Int): StickerAction
    data class UpdateCurrentPath(val offset: Offset): StickerAction
    object AddPath: StickerAction
}
