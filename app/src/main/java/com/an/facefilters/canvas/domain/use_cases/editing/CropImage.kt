package com.an.facefilters.canvas.domain.use_cases.editing

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.an.facefilters.canvas.domain.model.Result
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.presentation.util.cropToRect

class CropImage {
    operator fun invoke(
        element: Element?,
        srcRect: Rect,
        viewSize: IntSize,
    ): Result<Img> {

        if(element == null) return Result.Failure("Element not Found")

        if(element !is Img) return Result.Failure("Pick Image")

        val croppedBitmap = element.bitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        val croppedOriginalBitmap = element.originalBitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        return Result.Success(element.copy(
            bitmap = croppedBitmap,
            originalBitmap = croppedOriginalBitmap
        ))
    }
}