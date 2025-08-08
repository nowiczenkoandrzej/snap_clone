package com.an.core_editor.domain.model

data class ImageDomainModel(
    override val rotationAngle: Float,
    override val scale: Float,
    override val position: Point,
    override val alpha: Float,
    val image: ImageData
): Element {
    override fun transform(scaleDelta: Float, rotationDelta: Float, translation: Point): Element {
        val newScale = (scale * scaleDelta).coerceIn(0.1f, 5f)

        return this.copy(
            scale = newScale,
            rotationAngle = rotationAngle + rotationDelta,
            position = position.plus(translation)
        )
    }

    override fun center(): Point {
        return Point(
            x = position.x + (image.width / 2),
            y = position.y + (image.height / 2)
        )
    }

    override fun setAlpha(alpha: Float): Element {
        return this.copy(
            alpha = alpha
        )
    }
}
