package com.an.core_editor.domain.model

import android.graphics.Bitmap
import android.graphics.RectF
import androidx.compose.ui.geometry.Rect
import java.util.UUID

data class DomainImageModel(
    val id: String = UUID.randomUUID().toString(),
    val imagePath: String,
    val viewRect: Rect? = null,
    val imageRect: Rect,
    val center: Point = Point(0f, 0f),
    override val rotationAngle: Float = 0f,
    override val scale: Float = 1f,
    override val position: Point = Point(0f, 0f),
    override val alpha: Float = 1f,
    val currentFilter: String = "Original",
    val drawingPaths: List<PathData> = emptyList(),
    val cutPaths: List<PathData> = emptyList(),
    val rubberPaths: List<PathData> = emptyList(),
    val subjectMask: BooleanArray? = null,
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
