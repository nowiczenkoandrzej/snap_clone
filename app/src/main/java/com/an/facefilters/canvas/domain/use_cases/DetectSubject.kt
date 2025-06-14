package com.an.facefilters.canvas.domain.use_cases

import android.graphics.Bitmap
import com.an.facefilters.canvas.data.SubjectDetector

class DetectSubject(
    private val subjectDetector: SubjectDetector
){
    operator fun invoke(
        bitmap: Bitmap,
        onDetect: (Bitmap?) -> Unit
    ) {

        subjectDetector.detectSubject(
            bitmap = bitmap,
            onError = { error ->
                throw DetectionException(error)
            },
            onSubjectDetected = { newBitmap ->
                onDetect(newBitmap)
            }
        )

    }
}

class DetectionException(message: String): Exception(message)