package com.an.feature_image_editing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times

@Composable
@ExperimentalMaterial3Api
fun SizeSlider(
    modifier: Modifier = Modifier,
    onValueChange: (Float) -> Unit,
    value: Float,
    minValue: Float = 12f,
    maxValue: Float = 120f,
) {

    val valueRange = minValue..maxValue

    Slider(
        value = value,
        valueRange = valueRange,
        onValueChange = { newValue ->
            onValueChange(newValue)
        },
        thumb = {
            val sizeDp = (value / valueRange.endInclusive) * 40.dp + 10.dp

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.Red.copy(alpha = 0f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val circleSize = ((value / valueRange.endInclusive) * 40.dp) + 10.dp
                Box(
                    modifier = Modifier
                        .size(circleSize)
                        .background(Color.Red, CircleShape)
                )
            }
        }

    )

}