package com.an.facefilters.canvas.presentation.components.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.an.facefilters.R
import com.an.facefilters.canvas.presentation.components.QuickColorPicker
import com.an.facefilters.ui.theme.spacing

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

    var showThicknessSelector by remember {
        mutableStateOf(false)
    }

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

                Box {
                    Spacer(
                        modifier = Modifier
                            .size(size)
                            .clickable {
                                showThicknessSelector = true
                            }
                            .drawBehind {
                                drawThicknessCurve(
                                    thickness = thickness,
                                    size = sizePx,
                                    color = color
                                )
                            }
                    )

                    DropdownMenu(
                        expanded = showThicknessSelector,
                        onDismissRequest = {
                            showThicknessSelector = false
                        },
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        for(i in 8..36 step 4) {
                            DropdownMenuItem(
                                text = {
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
                                            },

                                        )
                                },
                                onClick = {
                                    onChangeThickness(i.toFloat())
                                    showThicknessSelector = false
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(MaterialTheme.spacing.small))
                    Text(
                        text = stringResource(R.string.size),
                        color = color
                    )
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

private fun DrawScope.drawThicknessCurve(
    thickness: Float,
    size: Float,
    color: Color
) {
    val path = Path().apply {
        moveTo(
            x = size,
            y = 0f
        )
        quadraticTo(
            x1 = size / 3,
            y1 = size / 3,
            x2 = size / 2,
            y2 = size / 2
        )
        quadraticTo(
            x1 = size / 3 * 2,
            y1 = size / 3 * 2,
            x2 = 0f,
            y2 = size
        )

    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round
        )
    )
}