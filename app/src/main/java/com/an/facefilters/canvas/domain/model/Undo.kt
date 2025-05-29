package com.an.facefilters.canvas.domain.model


data class Undo(
    val layers: List<Element>,
    val paths: List<PathData>
)
