package com.an.core_editor.domain

import androidx.compose.ui.graphics.Color

data class DomainColor(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float = 255f,
) {
    companion object {
        val BLACK = DomainColor(0f,0f,0f)
    }
}
