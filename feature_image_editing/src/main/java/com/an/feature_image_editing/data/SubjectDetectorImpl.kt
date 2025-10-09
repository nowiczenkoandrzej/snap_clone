package com.an.feature_image_editing.data

import android.graphics.Bitmap
import com.an.feature_image_editing.domain.SubjectDetector
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
        onSubjectDetected: (BooleanArray) -> Unit,
        onError: (String) -> Unit
    ) {

        val image = InputImage.fromBitmap(
            bitmap,
            0
        )

        segmenter.process(image)
            .addOnSuccessListener { result ->
                val fg = result.foregroundBitmap ?: return@addOnSuccessListener
                val mask = BooleanArray(fg.width * fg.height)
                val pixels = IntArray(fg.width * fg.height)
                fg.getPixels(pixels, 0, fg.width, 0, 0, fg.width, fg.height)
                for (i in pixels.indices) {
                    val alpha = pixels[i] ushr 24 and 0xFF
                    mask[i] = alpha > 128
                }
                onSubjectDetected(mask)

            }
            .addOnFailureListener { e ->
                onError(e.message.toString())
            }

    }
}