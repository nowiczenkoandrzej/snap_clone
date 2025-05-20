package com.an.facefilters.canvas.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

sealed interface ToolType {
    object AddPhoto: ToolType
    object Pencil: ToolType
    object Text: ToolType
    object RemoveBg: ToolType

}

data class Tool(
    val icon: ImageVector,
    val type: ToolType,
    val name: String
)
