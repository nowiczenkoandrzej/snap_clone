package com.an.core_editor.domain.model

import androidx.compose.ui.graphics.Color
import com.an.core_editor.domain.DomainColor

data class PathData(
    val color: DomainColor,
    val path: List<Point>,
    val thickness: Float
) {
    companion object {
        val DEFAULT = PathData(
            color = DomainColor.BLACK,
            path = emptyList(),
            thickness = 14F
        )
    }

    fun reset(): PathData {
        return this.copy(
            path = emptyList()
        )
    }

}
