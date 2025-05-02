package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.an.facefilters.R

@Composable
fun BottomActionsPanel(
    modifier: Modifier = Modifier,
    onLayersClick: () -> Unit,
    onToolsClick: () -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            onClick = { onLayersClick() }
        ) {
            Text(stringResource(R.string.layers))
        }
        TextButton(
            onClick = { onToolsClick() }
        ) {
            Text(stringResource(R.string.tools))
        }
        IconButton(
            onClick = { onUndo() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null
            )
        }
        IconButton(
            onClick = { onRedo() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }

    }

}