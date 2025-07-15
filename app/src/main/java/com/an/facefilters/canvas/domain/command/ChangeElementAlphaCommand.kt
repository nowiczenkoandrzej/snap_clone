package com.an.facefilters.canvas.domain.command

import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.use_cases.editing.ChangeElementAlpha

class ChangeElementAlphaCommand(
    private val elements: List<Element>,
    private val element: Element?,
    private val alpha: Float,
    private val changeAlphaUseCase: ChangeElementAlpha
): Command<List<Element>> {
    override fun execute(): Result<List<Element>> {
        val result = changeAlphaUseCase(
            element = element,
            alpha = alpha
        )
        return when(result){
            is Result.Failure -> Result.Failure(result.message)
            is Result.Success<Element> -> Result.Success(elements + result.data)
        }
    }

    override fun undo(): Result<List<Element>> {
        val result = changeAlphaUseCase(
            element = element,
            alpha = element?.alpha ?: 1f
        )
        return when(result){
            is Result.Failure -> Result.Failure(result.message)
            is Result.Success<Element> -> Result.Success(elements + result.data)
        }
    }
}