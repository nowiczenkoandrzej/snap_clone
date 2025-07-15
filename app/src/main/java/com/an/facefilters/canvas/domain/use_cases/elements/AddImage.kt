package com.an.facefilters.canvas.domain.use_cases.elements

import android.graphics.Bitmap
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Result

class AddImage {
    operator fun invoke(
        list: List<Element>,
        img: Img?
    ): Result<List<Element>> {
        if(img == null) return  Result.Failure("Element not found.")

        return Result.Success(list + img)
    }
}