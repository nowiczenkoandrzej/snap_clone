package com.an.facefilters.canvas.domain

import android.graphics.Bitmap

interface SubjectDetector {
    fun detectSubject(
        bitmap: Bitmap,
        onSubjectDetected: (Bitmap) -> Unit,
        onError: (String) -> Unit
    )
}