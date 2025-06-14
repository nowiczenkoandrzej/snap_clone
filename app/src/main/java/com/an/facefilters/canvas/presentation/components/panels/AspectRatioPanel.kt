package com.an.facefilters.canvas.presentation.components.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.an.facefilters.canvas.presentation.util.AspectRatio

@Composable
fun AspectRatioPanel(
    modifier: Modifier = Modifier,
    onAspectRatioSelected: (Float) -> Unit
) {
    Row(
       modifier = Modifier
           .fillMaxWidth()
           .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        AspectRatio.ALL_RATIOS.forEach { (name, value) ->
            Button(
                onClick = { onAspectRatioSelected(value) }
            ) {
                Text(name)
            }
        }
    }
}