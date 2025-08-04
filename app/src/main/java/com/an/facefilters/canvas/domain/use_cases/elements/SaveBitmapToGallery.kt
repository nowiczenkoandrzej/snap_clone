package com.an.facefilters.canvas.domain.use_cases.elements

import com.an.facefilters.canvas.domain.managers.PngFileManager
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.presentation.util.AspectRatio
import java.io.File

class SaveBitmapToGallery(
    private val fileManager: PngFileManager,
) {
    suspend operator fun invoke(
        elements: List<Element>,
        width: Int,
        height: Int
    ): Result<String> {

        val bitmap = fileManager.saveElementsAsBitmap(
            elements = elements,
            width = width,
            height = height
        )

        val file = fileManager.saveBitmapToGalleryAsFile(bitmap)

        if(file != null) {
            return Result.Success("Saved to gallery")
        } else {
            return Result.Failure("Failed to save to gallery")
        }

    }
}