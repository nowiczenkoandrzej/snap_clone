package com.an.facefilters.canvas.domain.model


data class Undo(
    val layers: List<Layer>,
    val paths: List<PathData>
)
