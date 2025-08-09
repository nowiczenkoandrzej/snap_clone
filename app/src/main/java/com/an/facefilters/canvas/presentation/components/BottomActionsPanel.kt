package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
    onElementsClick: () -> Unit,
    onToolsClick: () -> Unit,
    onUndo: () -> Unit,
) {
    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextButton(
            onClick = { onElementsClick() }
        ) {
            Text(
                text = stringResource(R.string.elements),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        TextButton(
            onClick = { onToolsClick() }
        ) {
            Text(
                text = stringResource(R.string.tools),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        IconButton(
            onClick = { onUndo() }
        ) {
            /*Icon(
                imageVector = Icons.Default.Undo,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )*/
        }

    }

}