package com.an.facefilters.canvas.domain

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Mode
import com.an.facefilters.canvas.domain.model.PathData
import com.an.facefilters.canvas.presentation.util.AspectRatio
import java.io.File

data class CanvasState (
    val elements: ElementsState = ElementsState(),
    val stickers: StickersState = StickersState(),
    val toolsUiState: ToolsUiState = ToolsUiState()
)

data class ElementsState(
    val elements: List<Element> = emptyList(),
    val selectedElement: Element? = null,
    val aspectRatio: Float = AspectRatio.RATIO_1_1
)

data class StickersState(
    val selectedCategory: StickerCategory = StickerCategory.EMOJIS,
    val categories: List<String> = emptyList(),
    val stickers: List<String> = emptyList(),
    val userStickers: List<File> = emptyList()
)

data class ToolsUiState(
    val showColorPicker: Boolean = false,
    val showTextInput: Boolean = false,
    val selectedFontFamily: FontFamily = FontFamily.Default,
    val selectedColor: Color = Color.Black,
    val showToolsSelector: Boolean = false,
    val selectedMode: Mode = Mode.ASPECT_RATIO,
)


