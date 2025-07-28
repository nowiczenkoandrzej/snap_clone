package com.an.facefilters.canvas.presentation.components.panels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import com.an.facefilters.canvas.presentation.components.FontSelector
import com.an.facefilters.canvas.presentation.components.QuickColorPicker

@Composable
fun TextPanel(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    selectedFont: FontFamily,
    onShowColorPicker: () -> Unit,
    onSelectFont: (FontFamily) -> Unit,
    onSelectColor: (Color) -> Unit
) {

    Column(
        modifier = modifier
    ) {
        FontSelector(
            selectedFont = selectedFont,
            onSelectFont = onSelectFont
        )
        QuickColorPicker(
            selectedColor = selectedColor,
            onColorSelected = onSelectColor,
            onOpenCustomColorPicker = onShowColorPicker,
            modifier = Modifier.fillMaxWidth()
        )
    }



}