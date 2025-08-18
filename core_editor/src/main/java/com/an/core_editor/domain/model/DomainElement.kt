package com.an.core_editor.domain.model

sealed interface DomainElement {
    val rotationAngle: Float
    val scale: Float
    val position: Point
    val alpha: Float

    fun transform(
        scaleDelta: Float,
        rotationDelta: Float,
        translation: Point
    ): DomainElement

    fun center(): Point

    fun setAlpha(alpha: Float): DomainElement


}
