package com.an.feature_stickers.presentation.util

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun CheckerboardBackgroundStickers(
    modifier: Modifier = Modifier,
    color1: Color = Color.LightGray,
    color2: Color = Color.White,
    cellSize: Float = 40f
) {
    Canvas(modifier = modifier) {
        val rows = (size.height / cellSize).toInt() + 1
        val cols = (size.width / cellSize).toInt() + 1

        for (row in 0..rows) {
            for (col in 0..cols) {
                drawRect(
                    color = if ((row + col) % 2 == 0) color1 else color2,
                    topLeft = Offset(col * cellSize, row * cellSize),
                    size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                )
            }
        }
    }
}