package com.an.core_editor.domain.model

import androidx.compose.ui.graphics.Color

data class PathData(
    val color: Color,
    val path: List<Point>,
    val thickness: Float
) {
    companion object {
        val DEFAULT = PathData(
            color = Color.Black,
            path = emptyList(),
            thickness = 14F
        )
    }

    fun reset(): PathData {
        return this.copy(
            path = emptyList()
        )
    }

    fun mergePath(): List<Point> {
        val points = path
    }
}
