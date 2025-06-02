package com.an.facefilters.canvas.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.PathData

data class CanvasState (
    val elements: List<Element> = emptyList(),
    val selectedElementIndex: Int? = null,
    val showToolsSelector: Boolean = false,
    val alphaSliderPosition: Float = 1f,
    val selectedMode: Mode = Mode.ELEMENTS,
    val drawnPath: PathData? = null,
    val paths: List<PathData> = emptyList(),
    val selectedColor: Color = Color.Black,
    val pathThickness: Float = 10f,
    val showColorPicker: Boolean = false,
    val showTextInput: Boolean = false,
    val selectedFontFamily: FontFamily = FontFamily.Default,
    val showDeleteElementIcon: Boolean = false
)


