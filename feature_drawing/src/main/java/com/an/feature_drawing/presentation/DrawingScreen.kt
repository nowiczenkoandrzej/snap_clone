package com.an.feature_drawing.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.an.core_editor.presentation.mappers.toOffsetList
import com.an.feature_drawing.presentation.components.DrawingArea
import com.an.feature_drawing.presentation.components.DrawingPanel
import com.an.feature_drawing.presentation.components.Magnifier
import com.an.feature_drawing.presentation.components.RubberPanel
import com.an.feature_drawing.presentation.util.DrawingMode
import kotlinx.coroutines.launch

@Composable
fun DrawingScreen(
    viewModel: DrawingViewModel,
    popBackStack: () -> Unit
) {

    val drawingState = viewModel
        .drawingState
        .collectAsState()
        .value

    val editedImage = viewModel
        .editedImageModel
        .collectAsState()
        .value

    val magnifierSize = 148.dp
    val magnification = 0.9f

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BackHandler {
        viewModel.onAction(DrawingAction.Cancel)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {

                DrawingEvent.Cancel -> {
                    popBackStack()
                }
                is DrawingEvent.ShowSnackbar -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(contentPadding),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val bitmap = editedImage?.bitmap
            if (bitmap != null) {

                Box(
                    modifier = Modifier.weight(5f)
                ) {
                    DrawingArea(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = editedImage.bitmap!!,
                        alpha = editedImage.alpha,
                        paths = drawingState.paths,
                        onDrawPath = { newOffset, scale ->
                            viewModel.onAction(DrawingAction.UpdateCurrentPath(newOffset, scale))
                        },
                        onFinishDrawingPath = {
                            viewModel.onAction(DrawingAction.AddNewPath)
                        },
                        currentPath = drawingState.currentPath
                    )

                    if(drawingState.showMagnifier && drawingState.fingerPosition != null) {
                        Magnifier(
                            modifier = Modifier.fillMaxSize(),
                            bitmap = bitmap,
                            fingerPosition = drawingState.fingerPosition,
                            alpha = 1f,
                            currentPath = drawingState.currentPath.path.toOffsetList()
                        )
                    }
                }



            }
            Column(
                modifier = Modifier.weight(2f),
            ) {

                when(drawingState.mode) {
                    DrawingMode.Cut -> {

                    }
                    DrawingMode.Eraser -> {
                        RubberPanel(
                            modifier = Modifier.fillMaxWidth(),
                            currentThickness = drawingState.pathThickness,
                            onThicknessChange = { newThickness ->
                                viewModel.onAction(DrawingAction.SelectThickness(newThickness))
                            },
                            onCancel = { viewModel.onAction(DrawingAction.Cancel) },
                            onSave = { viewModel.onAction(DrawingAction.SaveDrawings) },
                            onUndo = { viewModel.onAction(DrawingAction.UndoPath) }
                        )
                    }
                    DrawingMode.Pencil -> {
                        DrawingPanel(
                            modifier = Modifier.fillMaxWidth(),
                            selectedColor = drawingState.selectedColor,
                            selectedThickness = drawingState.pathThickness,
                            onShowColorPicker = {},
                            onColorSelected = { color ->
                                viewModel.onAction(DrawingAction.SelectColor(color))
                            },
                            onChangeThickness = { thickness ->
                                viewModel.onAction(DrawingAction.SelectThickness(thickness))
                            },
                            onCancel = {
                                viewModel.onAction(DrawingAction.Cancel)
                            },
                            onSave = {
                                viewModel.onAction(DrawingAction.SaveDrawings)
                            },
                            onUndoPath = {
                                viewModel.onAction(DrawingAction.UndoPath)
                            }
                        )
                    }
                }


            }
        }

    }


}