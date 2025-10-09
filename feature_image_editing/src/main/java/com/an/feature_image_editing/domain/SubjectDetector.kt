package com.an.feature_image_editing.domain

import android.graphics.Bitmap

interface SubjectDetector {
    fun detectSubject(
        bitmap: Bitmap,
        onSubjectDetected: (BooleanArray) -> Unit,
        onError: (String) -> Unit
    )
}