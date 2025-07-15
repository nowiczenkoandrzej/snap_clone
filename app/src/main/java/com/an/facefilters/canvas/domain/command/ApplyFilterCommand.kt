package com.an.facefilters.canvas.domain.command

import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.data.filters.getFiltersList
import com.an.facefilters.canvas.data.filters.getPhotoFilterByName
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.use_cases.editing.ApplyFilter
import com.an.facefilters.canvas.domain.use_cases.elements.ElementsUseCases

class ApplyFilterCommand(
    private val elements: List<Element>,
    private val imageElement: Img,
    private val filter: PhotoFilter,
    private val applyFilterUseCase: ApplyFilter,
): Command<List<Element>> {
    override fun execute(): Result<List<Element>> {
        val result = applyFilterUseCase(
            element = imageElement,
            filter = filter
        )
        return when(result) {
            is Result.Failure -> Result.Failure(result.message)
            is Result.Success<Img> -> Result.Success(elements + result.data)
        }
    }

    override fun undo(): Result<List<Element>> {
        val prevFilter = getPhotoFilterByName(imageElement.currentFilter)
        val result = applyFilterUseCase(
            element = imageElement,
            filter = prevFilter
        )
        return when(result) {
            is Result.Failure -> Result.Failure(result.message)
            is Result.Success<Img> -> Result.Success(elements + result.data)
        }
    }

}