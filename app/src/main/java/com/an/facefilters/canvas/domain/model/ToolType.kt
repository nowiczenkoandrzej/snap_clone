package com.an.facefilters.canvas.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface ToolType {
    object AddPhoto: ToolType
    object Pencil: ToolType
    object Text: ToolType
    object RemoveBg: ToolType
    object CropImage: ToolType
    object CreateSticker: ToolType
    object AspectRatio: ToolType
    object Stickers: ToolType
    object Filters: ToolType
}

data class Tool(
    val icon: ImageVector,
    val type: ToolType,
    val name: String
)
