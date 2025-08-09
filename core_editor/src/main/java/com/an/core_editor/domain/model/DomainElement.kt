package com.an.core_editor.domain.model

interface Element {
    val rotationAngle: Float
    val scale: Float
    val position: Point
    val alpha: Float

    fun transform(
        scaleDelta: Float,
        rotationDelta: Float,
        translation: Point
    ): Element

    fun center(): Point

    fun setAlpha(alpha: Float): Element


}