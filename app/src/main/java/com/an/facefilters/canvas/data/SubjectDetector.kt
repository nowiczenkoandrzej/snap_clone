package com.an.facefilters.canvas.data

import android.graphics.Bitmap
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.subject.SubjectSegmentation
import com.google.mlkit.vision.segmentation.subject.SubjectSegmenterOptions

class SubjectDetector {

    private val opts = SubjectSegmenterOptions.Builder()
        .enableForegroundBitmap()
        .build()

    private val segmenter = SubjectSegmentation.getClient(opts)

    @OptIn(ExperimentalGetImage::class)
    fun detectSubject(
        bitmap: Bitmap,
        onSubjectDetected: (Bitmap) -> Unit,
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

            }

    }


}