package com.an.facefilters.canvas.domain.model

import com.an.facefilters.canvas.domain.PathData

data class Undo(
    val layers: List<Layer>,
    val paths: List<PathData>
)
