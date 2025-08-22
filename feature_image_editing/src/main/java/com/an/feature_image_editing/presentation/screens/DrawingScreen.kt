package com.an.feature_image_editing.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.presentation.toUiImageModel
import com.an.feature_image_editing.presentation.DrawingAction
import com.an.feature_image_editing.presentation.EditingEvent
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.components.DrawingPanel
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
                        
                        ImagePreview(
                            bitmap = bitmap,
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(2f)
                ) {
                    DrawingPanel(
                        modifier = Modifier.fillMaxWidth(),
                        selectedColor = drawingState.selectedColor,
                        thickness = drawingState.pathThickness,
                        onShowColorPicker = {},
                        onColorSelected = { color ->
                            viewModel.onAction(DrawingAction.SelectColor(color))
                        },
                        onChangeThickness = { thickness ->
                            viewModel.onAction(DrawingAction.SelectThickness(thickness))
                        },
                        onCancel = {
                            
                        },
                        onSave = {

                        },
                        onUndoPath = {

                        }
                    )
                }

            }

        }
    }

}