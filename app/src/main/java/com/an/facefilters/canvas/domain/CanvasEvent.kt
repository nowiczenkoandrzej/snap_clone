package com.an.facefilters.canvas.domain

sealed interface CanvasEvent {
    object None : CanvasEvent
    object PickImage: CanvasEvent
    object NavigateToCropScreen: CanvasEvent
    object ImageCropped: CanvasEvent
    object StickerCreated: CanvasEvent
    object StickerAdded: CanvasEvent
    object NavigateToCreateStickerScreen: CanvasEvent
    object NavigateToStickersScreen: CanvasEvent
    data class ShowToast(val message: String): CanvasEvent

}