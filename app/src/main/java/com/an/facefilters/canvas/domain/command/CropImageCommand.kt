package com.an.facefilters.canvas.domain.command

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.use_cases.editing.CropImage

class CropImageCommand(
    private val image: Img?,
    private val cropImageUseCase: CropImage,
    private val srcRect: Rect,
    private val viewSize: IntSize
): Command<List<Element>> {
    override fun execute(): Result<List<Element>> {
        val result = cropImageUseCase(
            element = image,
            srcRect = srcRect,
            viewSize = viewSize
        )
        return when(result) {
            is Result.Failure -> Result.Failure(result.message)
            is Result.Success<Img> -> TODO()
        }
    }

    override fun undo(): Result<List<Element>> {
        TODO("Not yet implemented")
    }
}