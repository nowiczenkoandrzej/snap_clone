package com.an.feature_image_editing.presentation.components.panels

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.an.feature_image_editing.presentation.components.QuickColorPicker
import com.an.feature_image_editing.presentation.util.drawThicknessCurve

@Composable
fun DrawingPanel(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    selectedThickness: Float,
    onShowColorPicker: () -> Unit,
    onColorSelected: (Color) -> Unit,
    onChangeThickness: (Float) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onUndoPath: () -> Unit
) {

    val onSurface = MaterialTheme.colorScheme.onSurface
    val primary = MaterialTheme.colorScheme.primary

    val size = 48.dp
    val sizePx = with(LocalDensity.current) { size.toPx() }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .background(MaterialTheme.colorScheme.surface),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items((8..36 step 4).toList()) { thickness ->
                        Canvas(
                            modifier = Modifier
                                .size(size)
                                .clickable { onChangeThickness(thickness.toFloat()) }
                        ) {
                            drawThicknessCurve(
                                thickness = thickness.toFloat(),
                                size = sizePx,
                                color = if (thickness.toFloat() == selectedThickness) primary else onSurface
                            )
                        }
                    }
                }

                QuickColorPicker(
                    selectedColor = selectedColor,
                    onColorSelected = { color ->
                        onColorSelected(color)
                    },
                    onOpenCustomColorPicker = { onShowColorPicker() },
                    modifier = Modifier
                )
            }



        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = { onCancel() }
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = null,
                    tint = onSurface
                )
            }
            IconButton(
                onClick = { onSave() }
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = onSurface
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { onUndoPath() }
            ) {
                Icon(
                    imageVector = Icons.Default.Undo,
                    contentDescription = null,
                    tint = onSurface
                )
            }
        }
    }
}