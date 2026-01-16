package com.an.feature_drawing.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.an.feature_drawing.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RubberPanel(
    modifier: Modifier = Modifier,
    currentThickness: Float,
    onThicknessChange: (Float) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onUndo: () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SizeSlider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onValueChange = { newThickness ->
                onThicknessChange(newThickness)
            },
            value = currentThickness,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onCancel() }
            ) {
                Text(stringResource(R.string.cancel))
            }
            TextButton(
                onClick = { onSave() }
            ) {
                Text(stringResource(R.string.save))
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

}