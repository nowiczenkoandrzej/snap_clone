package com.an.feature_image_editing.presentation

sealed interface EditingEvent {
    object PopBackStack: EditingEvent
    data class ShowSnackbar(val message: String): EditingEvent
}