package com.an.facefilters.canvas.domain

import com.an.facefilters.canvas.domain.model.Layer
import com.an.facefilters.canvas.domain.model.Mode

data class CanvasState (
    val layers: List<Layer> = emptyList(),
    val selectedLayerIndex: Int? = null,
    val showToolsBottomSheet: Boolean = false,
    val alphaSliderPosition: Float = 0.7f,
    val selectedMode: Mode = Mode.LAYERS
)
