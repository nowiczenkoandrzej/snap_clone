package com.an.feature_text.presentation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.an.core_editor.R
import com.an.core_editor.presentation.FontItem

data class TextScreenState(
    val textColor: Color = Color.Black,
    val fontItem: FontItem = FontItem(
        fontFamily = FontFamily.Default,
        name = "Lobster Two",
        fontResId = R.font.lobstertwo_regular
    ),
    val currentText: String = "",
    val showColorPicker: Boolean = false
)