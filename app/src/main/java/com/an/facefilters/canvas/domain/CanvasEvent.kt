package com.an.facefilters.canvas.domain

sealed interface CanvasEvent {
    object PickImage: CanvasEvent
    object NavigateToCropScreen: CanvasEvent
    object ImageCropped: CanvasEvent
    object StickerCreated: CanvasEvent
    object NavigateToCreateStickerScreen: CanvasEvent
    data class ShowToast(val message: String): CanvasEvent

}