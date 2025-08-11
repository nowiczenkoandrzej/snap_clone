package com.an.feature_canvas.presentation

import com.an.feature_canvas.domain.AspectRatio

data class CanvasState(
    val showToolsSelector: Boolean = false,
    val aspectRatio: Float = AspectRatio.RATIO_3_4,
    val showElementDetail: Boolean = false,
)
