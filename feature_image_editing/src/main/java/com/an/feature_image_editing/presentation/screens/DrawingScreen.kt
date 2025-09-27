package com.an.feature_image_editing.presentation.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.an.feature_image_editing.presentation.DrawingAction
import com.an.feature_image_editing.presentation.EditingEvent
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.components.DrawingArea
import com.an.feature_image_editing.presentation.components.panels.DrawingPanel
import com.an.feature_image_editing.presentation.components.ImagePreview
import com.an.feature_image_editing.presentation.util.drawPencil
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(5f),
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