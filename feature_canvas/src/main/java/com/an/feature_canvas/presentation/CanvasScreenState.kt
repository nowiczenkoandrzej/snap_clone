package com.an.feature_canvas.presentation

import com.an.feature_canvas.domain.AspectRatio
import com.an.feature_canvas.presentation.util.PanelMode

data class CanvasScreenState(
    val showToolsSelector: Boolean = false,
    val aspectRatio: Float = AspectRatio.RATIO_3_4,
    val showElementDetail: Boolean = false,
    val panelMode: PanelMode = PanelMode.ELEMENTS
)
