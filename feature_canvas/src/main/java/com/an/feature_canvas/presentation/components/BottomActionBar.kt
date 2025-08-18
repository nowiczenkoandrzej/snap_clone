package com.an.feature_canvas.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.an.feature_canvas.R

@Composable
fun BottomActionBar(
    modifier: Modifier = Modifier,
    onShowToolSelector: () -> Unit,
    onUndo: () -> Unit,
    onShowElementPanel: () -> Unit
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = { onShowElementPanel() }
        ) {
            Text(stringResource(R.string.elements))
        }
        TextButton(
            onClick = { onShowToolSelector() }
        ) {
            Text(stringResource(R.string.tools))
        }

        Spacer(modifier = Modifier.weight(1f))

        IconButton(
            onClick = { onUndo() }
        ) {
            Icon(
                imageVector = Icons.Default.Undo,
                contentDescription = null
            )
        }
    }

}