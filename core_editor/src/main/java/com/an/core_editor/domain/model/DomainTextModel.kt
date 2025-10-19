package com.an.core_editor.domain.model

import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily

data class DomainTextModel(
    override val rotationAngle: Float,
    override val scale: Float,
    override val position: Point,
    override val alpha: Float,
    val text: String,
    val fontSize: Float,
    val fontColor: DomainColor,
    val fontFamily: DomainFontFamily
): DomainElement {
    override fun transform(scaleDelta: Float, rotationDelta: Float, translation: Point): DomainElement {
        val newScale = (scale * scaleDelta).coerceIn(0.1f, 10f)

        return this.copy(
            scale = newScale,
            rotationAngle = rotationAngle + rotationDelta,
            position = position.plus(translation)
        )
    }

    override fun setAlpha(alpha: Float): DomainElement {
        return this.copy(alpha = alpha)
    }
}
