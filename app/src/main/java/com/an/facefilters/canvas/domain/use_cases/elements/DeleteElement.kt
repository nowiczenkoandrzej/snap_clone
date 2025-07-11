package com.an.facefilters.canvas.domain.use_cases.elements

import com.an.facefilters.canvas.domain.Result
import com.an.facefilters.canvas.domain.model.Element

class DeleteElement {
    operator fun invoke(
        list: List<Element>,
        element: Element?
    ): Result<List<Element>> {

        if(list.isEmpty()) return Result.Failure("Element not Found")

        if(element == null) return Result.Failure("Element not Found")

        if(!list.contains(element)) return Result.Failure("Element not Found")


        val newList = list.toMutableList()
            .apply {
                remove(element)
            }
            .toList()

        return Result.Success(newList)
    }
}