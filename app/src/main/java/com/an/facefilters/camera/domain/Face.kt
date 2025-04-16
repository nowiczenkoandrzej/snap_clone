package com.an.facefilters.camera.domain

import android.graphics.PointF

data class Face(
    val faceOval: List<PointF> = emptyList(),
    val leftEyebrowTop: List<PointF> = emptyList(),
    val leftEyebrowBottom: List<PointF> = emptyList(),
    val rightEyebrowTop: List<PointF> = emptyList(),
    val rightEyebrowBottom: List<PointF> = emptyList(),
    val leftEye: List<PointF> = emptyList(),
    val rightEye: List<PointF> = emptyList(),
    val upperLipTop: List<PointF> = emptyList(),
    val upperLipBottom: List<PointF> = emptyList(),
    val lowerLipTop: List<PointF> = emptyList(),
    val lowerLipBottom: List<PointF> = emptyList(),
    val noseBridge: List<PointF> = emptyList(),
    val noseBottom: List<PointF> = emptyList(),
    val leftCheek: List<PointF> = emptyList(),
    val rightCheek: List<PointF> = emptyList(),
)
