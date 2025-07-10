package com.an.facefilters.canvas.domain.use_cases.editing

import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.domain.Result
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img

class ApplyFilter {
    operator fun invoke(
        element: Element,
        filter: PhotoFilter
    ): Result<Img> {

        if(element !is Img)
            return Result.Failure(message = "Pick Image")
        else {
            val newImg = element.copy(
                bitmap = filter.apply(element.originalBitmap),
                currentFilter = filter.name
            )
            return Result.Success(newImg)
        }
    }
}