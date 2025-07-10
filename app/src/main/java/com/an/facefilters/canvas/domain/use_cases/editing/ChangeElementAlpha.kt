package com.an.facefilters.canvas.domain.use_cases.editing

import com.an.facefilters.canvas.domain.model.Element

class ChangeElementAlpha {
    operator fun invoke(
        element: Element,
        alpha: Float
    ): Element {
        return element.setAlpha(alpha)
    }
}