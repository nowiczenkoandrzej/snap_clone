package com.an.facefilters.canvas.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.an.facefilters.canvas.domain.managers.StickerCategory
import com.an.facefilters.canvas.domain.model.CanvasMode
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.PanelMode
import com.an.facefilters.canvas.presentation.util.AspectRatio
import java.io.File

data class CanvasState (
    val elements: ElementsState = ElementsState(),
    val stickers: StickersState = StickersState(),
    val toolsUiState: UiState = UiState()
)

data class ElementsState(
    val elements: List<Element> = emptyList(),
    val selectedElementIndex: Int? = null,
    val editedElement: Element? = null
)

data class StickersState(
    val selectedCategory: StickerCategory = StickerCategory.EMOJIS,
    val categories: List<String> = emptyList(),
    val stickers: List<String> = emptyList(),
    val userStickers: List<File> = emptyList()
)

data class UiState(
    val showColorPicker: Boolean = false,
    val showTextInput: Boolean = false,
    val selectedFontFamily: FontFamily = FontFamily.Default,
    val selectedColor: Color = Color.Black,
    val showToolsSelector: Boolean = false,
    val aspectRatio: Float = AspectRatio.RATIO_1_1,
    val selectedPanelMode: PanelMode = PanelMode.ASPECT_RATIO,
    val selectedCanvasMode: CanvasMode = CanvasMode.DEFAULT

)


