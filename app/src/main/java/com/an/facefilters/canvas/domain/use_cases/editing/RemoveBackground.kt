package com.an.facefilters.canvas.domain.use_cases.editing

import com.an.facefilters.canvas.data.DetectionException
import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img

class RemoveBackground(
    private val subjectDetector: SubjectDetector
){
    operator fun invoke(
        element: Element?,
        onDetect: (Img?) -> Unit
    ) {
        if(element == null) throw DetectionException("Pick Image")
        if(element !is Img) throw DetectionException("Pick Image")

        subjectDetector.detectSubject(
            bitmap = element.bitmap,
            onError = { error ->
                throw DetectionException(error)
            },
            onSubjectDetected = { newBitmap ->
                onDetect(element.copy(bitmap = newBitmap))
            }
        )
    }
}

