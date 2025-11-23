package com.an.core_editor.presentation.model

import androidx.compose.ui.geometry.Offset

interface UiElement {
    val rotationAngle: Float
    val scale: Float
    val alpha: Float
    val position: Offset

    fun center(): Offset
}