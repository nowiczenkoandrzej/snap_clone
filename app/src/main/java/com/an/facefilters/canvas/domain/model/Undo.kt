package com.an.facefilters.canvas.domain.model


data class Undo(
    val elements: List<Element>,
    val paths: List<PathData>
)
