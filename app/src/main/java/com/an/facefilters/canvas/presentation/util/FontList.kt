package com.an.facefilters.canvas.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.font.FontFamily

@Composable
fun rememberFontList(): List<FontItem> {


    val fonts = listOf(
        FontItem(
            fontFamily = FontFamily.Default,
            name = "Default"
        ),
        FontItem(
            fontFamily = FontFamily.Serif,
            name = "Serif"
        ),
        FontItem(
            fontFamily = FontFamily.Cursive,
            name = "Cursive"
        ),
        FontItem(
            fontFamily = FontFamily.Monospace,
            name = "Monospace"
        ),
        FontItem(
            fontFamily = FontFamily.SansSerif,
            name = "SansSerif"
        ),
    )

    return remember { fonts }

}

data class FontItem(
    val name: String,
    val fontFamily: FontFamily
)