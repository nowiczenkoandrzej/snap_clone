package com.an.facefilters.canvas.presentation.handlers

import com.an.facefilters.canvas.domain.DrawingAction

class DrawingHandler {

    fun handle(action: DrawingAction) {
        when(action) {
            is DrawingAction.DrawPath -> {

            }
            DrawingAction.EndDrawingPath -> {

            }
            DrawingAction.StartDrawingPath -> {

            }
        }
    }

}