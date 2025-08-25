package com.an.core_editor.domain.model

data class DomainStickerModel(
    override val rotationAngle: Float,
    override val scale: Float,
    override val position: Point,
    override val alpha: Float,
    val stickerPath: String,
): DomainElement {
    override fun transform(
        scaleDelta: Float,
        rotationDelta: Float,
        translation: Point,
    ): DomainElement {
        val newScale = (scale * scaleDelta).coerceIn(0.1f, 5f)

        return this.copy(
            scale = newScale,
            rotationAngle = rotationAngle + rotationDelta,
            position = position.plus(translation)
        )
    }

    override fun setAlpha(alpha: Float): DomainElement {
        return this.copy(
            alpha = alpha
        )
    }
}