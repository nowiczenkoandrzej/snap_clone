package com.an.facefilters.canvas.presentation

sealed interface CanvasEvent {
    object PickImage: CanvasEvent
    object StickerAdded: CanvasEvent
    object NavigateToDrawingScreen: CanvasEvent
    object NavigateToStickersScreen: CanvasEvent
    data class ShowSnackbar(val message: String): CanvasEvent

}