package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AspectRatioItem(
    label: String,
    ratio: Float,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
    val borderWidth = if (isSelected) 3.dp else 1.dp
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .size(width = 60.dp, height = (60.dp / ratio).coerceAtMost(100.dp)) // max height
            .border(borderWidth, borderColor, RoundedCornerShape(4.dp))
            .background(backgroundColor, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

}