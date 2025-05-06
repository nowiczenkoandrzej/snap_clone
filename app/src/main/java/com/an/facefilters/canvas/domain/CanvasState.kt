package com.an.facefilters.canvas.domain

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.an.facefilters.canvas.domain.model.Layer
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.PathData

data class CanvasState (
    val layers: List<Layer> = emptyList(),
    val selectedLayerIndex: Int? = null,
    val showToolsSelector: Boolean = false,
    val alphaSliderPosition: Float = 0.7f,
    val selectedMode: Mode = Mode.LAYERS,
    val drawnPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val selectedColor: Color = Color.Black,
    val pathThickness: Float = 10f,
    val showColorPicker: Boolean = false,
    val showTextInput: Boolean = false,
)

