package com.an.facefilters.canvas.data

import android.graphics.Bitmap
import com.an.facefilters.canvas.domain.SubjectDetector

class FakeSubjectDetector(
    private val shouldSucceed: Boolean = true,
    private val bitmapToReturn: Bitmap? = null,
    private val errorMessage: String = "Detection failed"
): SubjectDetector {
    override fun detectSubject(
        bitmap: Bitmap,
        onSubjectDetected: (Bitmap) -> Unit,
        onError: (String) -> Unit,
    ) {
        if (shouldSucceed && bitmapToReturn != null) {
            onSubjectDetected(bitmapToReturn)
        } else {
            onError(errorMessage)
        }
    }
}