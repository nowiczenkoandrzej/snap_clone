package com.an.feature_text.presentation

sealed interface TextEvent {
    object PopNavStack: TextEvent
    data class ShowSnackbar(val message: String): TextEvent
}