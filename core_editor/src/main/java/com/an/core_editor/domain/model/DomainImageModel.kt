package com.an.core_editor.domain.model

import com.an.core_editor.data.edits.ImageEdit
import com.an.core_editor.domain.DomainImageEdit
import java.util.UUID

data class DomainImageModel(
    val id: String = UUID.randomUUID().toString(),
    val imagePath: String,
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    override val position: Point = Point(0f, 0f),
    override val alpha: Float = 1f,
    val edits: List<DomainImageEdit> = emptyList(),
    val currentFilter: String = "Original",
    val version: Long = System.currentTimeMillis()
): DomainElement {
    override fun transform(scaleDelta: Float, rotationDelta: Float, translation: Point): DomainElement {
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
