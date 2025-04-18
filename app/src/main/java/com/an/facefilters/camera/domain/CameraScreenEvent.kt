package com.an.facefilters.camera.domain

sealed class CameraScreenEvent {
    object SwitchToFrontCamera: CameraScreenEvent()
    object SwitchToBackCamera: CameraScreenEvent()
    object NavigateToCanvas: CameraScreenEvent()
}