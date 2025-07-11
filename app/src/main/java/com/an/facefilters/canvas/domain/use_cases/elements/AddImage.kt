package com.an.facefilters.canvas.domain.use_cases.elements

import android.graphics.Bitmap
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img

class AddImage {
    operator fun invoke(
        list: List<Element>,
        bitmap: Bitmap
    ): List<Element> {
        return list + Img(
            bitmap = bitmap,
            originalBitmap = bitmap
        )
    }
}