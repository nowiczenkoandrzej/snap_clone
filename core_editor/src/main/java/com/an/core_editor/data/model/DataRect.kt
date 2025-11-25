package com.an.core_editor.data.model

import androidx.compose.ui.geometry.Rect
import kotlinx.serialization.Serializable

@Serializable
data class DataRect(
    val left: Float,
    val bottom: Float,
    val right: Float,
    val top: Float,
)
