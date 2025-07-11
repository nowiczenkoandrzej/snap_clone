package com.an.facefilters.canvas.domain.use_cases.editing

import com.an.facefilters.canvas.domain.Result
import com.an.facefilters.canvas.domain.model.Element

class ChangeElementAlpha {
    operator fun invoke(
        element: Element?,
        alpha: Float
    ): Result<Element> {
        if(element == null) return Result.Failure("Element not Found")

        return Result.Success(element.setAlpha(alpha))
    }
}