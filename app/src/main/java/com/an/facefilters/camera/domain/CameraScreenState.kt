package com.an.facefilters.camera.domain

import android.graphics.PointF

data class CameraScreenState(
    val delta: Float = 0f,
    val scale: Float = 0f,
    val screenWidth: Int = 0,
    val screenHeight: Int = 0,
    val imageWidth: Int = 0,
    val imageHeight: Int = 0,
    val contours: List<PointF> = emptyList()
)