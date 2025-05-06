package com.an.facefilters.canvas.domain

sealed interface CanvasEvent {
    object PickImage: CanvasEvent
}