package com.an.facefilters.canvas.domain.use_cases.editing

import androidx.compose.ui.geometry.Offset
import com.an.facefilters.canvas.domain.model.Element

class TransformElement {

    operator fun invoke(
        element: Element,
        scale: Float,
        rotation: Float,
        offset: Offset
    ): Element {
        return element.transform(
            scale = scale,
            rotation = rotation,
            offset = offset
        )
    }
}