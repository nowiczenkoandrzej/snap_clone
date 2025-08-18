package com.an.feature_image_editing.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.an.core_ui.ui.theme.spacing
import com.an.feature_image_editing.R
import com.an.feature_image_editing.presentation.util.drawThicknessCurve

@Composable
fun DrawingPanel(
    modifier: Modifier = Modifier,
    selectedColor: Color,
    thickness: Float,
    onShowColorPicker: () -> Unit,
    onColorSelected: (Color) -> Unit,
    onChangeThickness: (Float) -> Unit,
    onCancel: () -> Unit,
    onSave: () -> Unit,
    onUndoPath: () -> Unit
) {

    val color = MaterialTheme.colorScheme.onSurface

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

                val size = MaterialTheme.spacing.large

                val sizePx = with(LocalDensity.current) { size.toPx() }

                LazyRow(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for(i in 8..36 step 4) {
                        item {
                            Text(
                                text = "",
                                color = color,
                                modifier = Modifier
                                    .drawBehind {
                                        drawThicknessCurve(
                                            thickness = i.toFloat(),
                                            size = sizePx,
                                            color = color
                                        )
                                    }
                                    .clickable {
                                        onChangeThickness(i.toFloat())
                                    }

                                )
                        }
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
                    tint = color
                )
            }
            IconButton(
                onClick = { onSave() }
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    tint = color
                )
            }
            Spacer(Modifier.weight(1f))
            IconButton(
                onClick = { onUndoPath() }
            ) {
                Icon(
                    imageVector = Icons.Default.Undo,
                    contentDescription = null,
                    tint = color
                )
            }
        }
    }
}