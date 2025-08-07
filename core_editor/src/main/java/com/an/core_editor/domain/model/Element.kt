package com.an.core_editor.domain

interface Element {
    val rotationAngle: Float
    val scale: Float
    val p1: Offset
    val alpha: Float

    fun transform(
        scale: Float,
        rotation: Float,
        offset: Offset
    ): Element

    fun pivot(): Offset

    fun setAlpha(alpha: Float): Element


}