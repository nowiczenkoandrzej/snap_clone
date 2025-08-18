package com.an.feature_text.presentation

import androidx.compose.ui.graphics.Color
import com.an.core_editor.presentation.FontItem

sealed interface TextAction {
    object AddText: TextAction
    data class ApplyFontFamily(val fontItem: FontItem): TextAction
    data class ApplyTextColor(val color: Color): TextAction
    data class Type(val text: String): TextAction
    object ToggleColorPicker: TextAction
    object Cancel: TextAction

    data class ChangeFontFamily(val fontItem: FontItem): TextAction
    data class ChangeFontColor(val color: Color): TextAction
}