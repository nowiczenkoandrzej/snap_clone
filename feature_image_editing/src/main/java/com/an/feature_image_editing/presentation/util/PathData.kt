package com.an.feature_image_editing.presentation.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

data class PathData(
    val color: Color,
    val path: List<Offset>,
    val thickness: Float
) {
    companion object {
        val DEFAULT = PathData(
            color = Color.Black,
            path = emptyList(),
            thickness = 14F
        )
    }

    fun reset(): PathData{
        return this.copy(
            path = emptyList()
        )
    }
}
