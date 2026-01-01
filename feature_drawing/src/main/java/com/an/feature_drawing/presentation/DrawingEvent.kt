package com.an.feature_drawing.presentation

sealed interface DrawingEvent {

    object Cancel: DrawingEvent
    data class ShowSnackbar(val message: String): DrawingEvent

}