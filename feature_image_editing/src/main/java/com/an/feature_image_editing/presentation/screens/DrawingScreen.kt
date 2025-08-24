package com.an.feature_image_editing.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import com.an.feature_image_editing.presentation.DrawingAction
import com.an.feature_image_editing.presentation.EditingEvent
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.components.DrawingArea
import com.an.feature_image_editing.presentation.components.panels.DrawingPanel
import com.an.feature_image_editing.presentation.components.ImagePreview
import kotlinx.coroutines.launch

@Composable
fun DrawingScreen(
    viewModel: ImageEditingViewModel,
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


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BackHandler {
        popBackStack()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                EditingEvent.PopBackStack -> {
                    popBackStack()
                }
                is EditingEvent.ShowSnackbar -> scope.launch {
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

            editedImage?.let { editedImage ->
                Column(
                    modifier = Modifier.weight(5f)
                ) {

                    val bitmap = editedImage.bitmap
                    if (bitmap != null) {

                        DrawingArea(
                            modifier = Modifier.fillMaxWidth(),
                            bitmap = editedImage.bitmap!!,
                            alpha = editedImage.alpha,
                            paths = drawingState.paths,
                            onDrawPath = { newOffset ->
                                viewModel.onAction(DrawingAction.UpdateCurrentPath(newOffset))
                            },
                            onFinishDrawingPath = {
                                viewModel.onAction(DrawingAction.AddNewPath)
                            },
                            currentPath = drawingState.currentPath
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(2f)
                ) {
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
                            popBackStack()
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