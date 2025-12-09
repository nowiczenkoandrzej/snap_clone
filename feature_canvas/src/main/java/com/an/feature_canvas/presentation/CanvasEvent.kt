package com.an.feature_canvas.presentation

sealed interface CanvasEvent {
    object NavigateToStickersScreen: CanvasEvent
    object NavigateToEditImageScreen: CanvasEvent
    object NavigateToAddTextScreen: CanvasEvent
    object NavigateToEditTextScreen: CanvasEvent
    object PickImageFromGallery: CanvasEvent
    data class AddImageFromSavedProject(
        val imagePath: String
    ): CanvasEvent
    data class ShowSnackbar(val message: String): CanvasEvent
}