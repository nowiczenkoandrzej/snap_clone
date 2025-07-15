package com.an.facefilters.canvas.domain.command

import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.use_cases.elements.AddImage
import com.an.facefilters.canvas.domain.use_cases.elements.DeleteElement

class AddImageCommand(
    private val elements: List<Element>,
    private val imageElement: Img?,
    private val addImageUseCase: AddImage,
    private val deleteElementUseCase: DeleteElement,
) : Command<List<Element>> {
    override fun execute(): Result<List<Element>> {
        return when (val result = addImageUseCase(
            list = elements,
            img = imageElement
        )) {
            is Result.Success -> Result.Success(result.data)
            is Result.Failure -> Result.Failure(result.message)
        }
    }

    override fun undo(): Result<List<Element>> {
        return when(val result = deleteElementUseCase(
            list = elements,
            element = imageElement
        )) {
            is Result.Success -> Result.Success(result.data)
            is Result.Failure -> Result.Failure(result.message)
        }
    }
}