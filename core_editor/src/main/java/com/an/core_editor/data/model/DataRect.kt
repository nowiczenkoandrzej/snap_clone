package com.an.core_editor.data.model

import androidx.compose.ui.geometry.Rect
import kotlinx.serialization.Serializable

@Serializable
data class DataRect(
    val left: Float,
    val bottom: Float,
    val right: Float,
    val top: Float,
) {
    fun toDomain() = Rect(
        left = left,
        top = top,
        right = right,
        bottom = bottom,
    )
}

fun Rect.toData() = DataRect(left, top, right, bottom)
