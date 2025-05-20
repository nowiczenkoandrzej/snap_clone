package com.an.facefilters.canvas.domain

sealed interface CanvasEvent {
    object PickImage: CanvasEvent
    data class ShowToast(val message: String): CanvasEvent
}