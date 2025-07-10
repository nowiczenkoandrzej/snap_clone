package com.an.facefilters.canvas.domain.use_cases.editing

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.unit.IntSize
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.presentation.util.cropToRect

class CropImage {
    operator fun invoke(
        element: Img,
        srcRect: Rect,
        viewSize: IntSize,
    ): Img {
        val croppedBitmap = element.bitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        val croppedOriginalBitmap = element.originalBitmap.cropToRect(
            srcRect = srcRect,
            viewSize = viewSize
        )

        return element.copy(
            bitmap = croppedBitmap,
            originalBitmap = croppedOriginalBitmap
        )
    }
}