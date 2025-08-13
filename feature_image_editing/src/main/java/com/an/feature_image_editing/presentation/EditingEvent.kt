package com.an.feature_canvas.presentation

sealed interface CanvasEvent {
    object NavigateToDrawingScreen: CanvasEvent
    object NavigateToStickersScreen: CanvasEvent
    object NavigateToRubberScreen: CanvasEvent
    object NavigateToEditingScreen: CanvasEvent
    object NavigateToTextScreen: CanvasEvent
    object PickImageFromGallery: CanvasEvent
    data class ShowSnackbar(val message: String): CanvasEvent
}