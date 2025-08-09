package com.an.facefilters.canvas.data

import android.graphics.Bitmap
import com.an.facefilters.canvas.domain.SubjectDetector
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions

class SubjectDetectorImpl: SubjectDetector {

    private val opts = SubjectSegmenterOptions.Builder()
        .enableForegroundBitmap()
        .build()

    private val segmenter = SubjectSegmentation.getClient(opts)

    override fun detectSubject(
        bitmap: Bitmap,
        onSubjectDetected: (Bitmap) -> Unit,
        onError: (String) -> Unit
    ) {

        val image = InputImage.fromBitmap(
            bitmap,
            0
        )

        segmenter.process(image)
            .addOnSuccessListener { result ->
                if(result.foregroundBitmap != null)
                    onSubjectDetected(result.foregroundBitmap!!)

            }
            .addOnFailureListener { e ->
                onError(e.message.toString())
            }

    }


}
class DetectionException(message: String): Exception(message)