package com.an.facefilters.canvas.domain.use_cases

import android.graphics.Bitmap
import com.an.facefilters.canvas.domain.CanvasState
import com.an.facefilters.canvas.domain.model.Img

class CropImage {
    operator fun invoke(
        state: CanvasState,
        cropped: Bitmap
    ): Img {
        when {
            state.selectedElementIndex == null -> {
                throw Exception("Something Went Wrong...")
            }
            state.elements[state.selectedElementIndex] !is Img -> {
                throw Exception("Something Went Wrong...")
            }
            else -> {
                val oldImg = state.elements[state.selectedElementIndex] as Img
                return Img(
                    p1 = oldImg.p1,
                    rotationAngle = oldImg.rotationAngle,
                    scale = oldImg.scale,
                    alpha = oldImg.alpha,
                    bitmap = cropped,
                    originalBitmap = oldImg.originalBitmap
                )
            }
        }
    }
}