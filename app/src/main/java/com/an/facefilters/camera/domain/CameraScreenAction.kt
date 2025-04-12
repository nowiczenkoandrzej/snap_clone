package com.an.facefilters.camera.domain

import androidx.camera.core.ImageProxy

sealed interface CameraScreenAction {
    object SwitchCamera: CameraScreenAction
    data class SetCameraSettings(
        val imageHeight: Int,
        val imageWidth: Int,
        val screenHeight: Int,
        val screenWidth: Int,
    ): CameraScreenAction
    data class ProcessImage(
        val imageProxy: ImageProxy
    ): CameraScreenAction
}