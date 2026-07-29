package com.an.core_project.presentation

sealed interface HomeScreenEvent {
    object StartNewProject: HomeScreenEvent
    object LoadProject: HomeScreenEvent
    data class ShowSnackBar(val message: String): HomeScreenEvent
}