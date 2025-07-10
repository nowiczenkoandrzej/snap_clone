package com.an.facefilters.canvas.domain.use_cases.elements

import com.an.facefilters.canvas.domain.Result
import com.an.facefilters.canvas.domain.model.Element

class UpdateElementsOrder {

    operator fun invoke(
        list: List<Element>,
        from: Int,
        to: Int
    ): Result<List<Element>> {

        val size = list.size

        if(from >= size || from < 0 || to >= size || to < 0) return Result.Failure("Element not Found")

        val newList = list
            .toMutableList()
            .apply {
                add(to, removeAt(from))
            }
            .toList()

        return Result.Success(newList)
    }
}