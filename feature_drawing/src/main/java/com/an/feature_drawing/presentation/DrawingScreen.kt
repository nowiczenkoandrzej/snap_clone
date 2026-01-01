package com.an.feature_drawing.presentation

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.an.core_editor.presentation.model.UiImageModel
import com.an.feature_drawing.presentation.components.DrawingArea
import com.an.feature_drawing.presentation.components.DrawingPanel

@Composable
fun DrawingScreen(
    state: DrawingState,
    viewModel: DrawingViewModel
) {


    val editedImage

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


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
            val bitmap = editedImage.bitmap
            if (bitmap != null) {

                DrawingArea(
                    bitmap = editedImage.bitmap!!,
                    alpha = editedImage.alpha,
                    paths = state.paths,
                    onDrawPath = { newOffset, scale ->
                        //viewModel.onAction(DrawingAction.UpdateCurrentPath(newOffset, scale))
                    },
                    onFinishDrawingPath = {
                        //viewModel.onAction(DrawingAction.AddNewPath)
                    },
                    currentPath = state.currentPath
                )
            }
            Column(
                modifier = Modifier.weight(2f),
            ) {
                DrawingPanel(
                    modifier = Modifier.fillMaxWidth(),
                    selectedColor = state.selectedColor,
                    selectedThickness = state.pathThickness,
                    onShowColorPicker = {},
                    onColorSelected = { color ->
                        //viewModel.onAction(DrawingAction.SelectColor(color))
                    },
                    onChangeThickness = { thickness ->
                        //viewModel.onAction(DrawingAction.SelectThickness(thickness))
                    },
                    onCancel = {
                        //popBackStack()
                    },
                    onSave = {
                        //viewModel.onAction(DrawingAction.SaveDrawings)
                    },
                    onUndoPath = {
                        //viewModel.onAction(DrawingAction.UndoPath)
                    }
                )
            }
        }

    }


}