package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun QuickColorPicker(
    selectedColor: Color,
    onColorSelected: (Color) -> Unit,
    onOpenCustomColorPicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val quickColors = listOf(
        Color.Black,
        Color.Red,
        Color.Green,
        Color.Blue,
        Color.Yellow,
        Color.Magenta
    )

    Row(
        modifier = modifier.padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        quickColors.forEach { color ->
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(
                        width = if (color == selectedColor) 3.dp else 1.dp,
                        color = if (color == selectedColor) Color.Gray else Color.LightGray,
                        shape = CircleShape
                    )
                    .background(color = color, shape = CircleShape)
                    .clickable { onColorSelected(color) }
            )
        }

        // Custom color picker button
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(
                    width = if (!quickColors.contains(selectedColor)) 3.dp else 1.dp,
                    color = if (!quickColors.contains(selectedColor)) Color.Gray else Color.LightGray,
                    shape = CircleShape
                )
                .background(Color.White, shape = CircleShape)
                .clickable { onOpenCustomColorPicker() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = "Custom Color",
                tint = Color.Black
            )
        }
    }

}