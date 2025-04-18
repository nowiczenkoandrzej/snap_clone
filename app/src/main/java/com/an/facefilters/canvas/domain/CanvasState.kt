package com.an.facefilters.canvas.domain

import com.an.facefilters.canvas.domain.model.Layer

data class CanvasState (
    val layers: List<Layer> = emptyList(),
    val selectedLayerIndex: Int? = null

)
