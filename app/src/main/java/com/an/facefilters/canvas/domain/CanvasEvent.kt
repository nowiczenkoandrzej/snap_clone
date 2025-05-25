package com.an.facefilters.canvas.domain

sealed interface CanvasEvent {
    object PickImage: CanvasEvent
    object CropImage: CanvasEvent
    object ImageCropped: CanvasEvent
    data class ShowToast(val message: String): CanvasEvent
}