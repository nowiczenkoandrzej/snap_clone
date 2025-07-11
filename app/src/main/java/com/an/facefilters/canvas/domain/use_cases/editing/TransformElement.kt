package com.an.facefilters.canvas.domain.use_cases.editing

import androidx.compose.ui.geometry.Offset
import com.an.facefilters.canvas.domain.Result
import com.an.facefilters.canvas.domain.model.Element

class TransformElement {

    operator fun invoke(
        element: Element?,
        scale: Float,
        rotation: Float,
        offset: Offset
    ): Result<Element> {

        if(element == null) return Result.Failure("Element not Found")

        return Result.Success(element.transform(
            scale = scale,
            rotation = rotation,
            offset = offset
        ))
    }
}