package com.an.feature_image_editing.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.an.feature_image_editing.presentation.EditingAction
import com.an.feature_image_editing.presentation.EditingEvent
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.components.panels.ImageActionPanel
import com.an.feature_image_editing.presentation.components.ImagePreview
import kotlinx.coroutines.launch

@Composable
fun ImageEditingScreen(
    viewModel: ImageEditingViewModel,
    onNavigateToDrawingScreen: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    onNavigateToCroppingScreen: () -> Unit,
    onNavigateToRubberScreen: () -> Unit,
    onNavigateToCreateStickerScreen: () -> Unit,
    popBackStack: () -> Unit
) {

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
                            alpha = editedImage.alpha
                        )
                    }
                }

                Column(
                    modifier = Modifier.weight(2f)
                ) {
                    ImageActionPanel(
                        onNavigateToDrawingScreen = onNavigateToDrawingScreen,
                        onNavigateToFilterScreen = onNavigateToFilterScreen,
                        onNavigateToCroppingScreen = onNavigateToCroppingScreen,
                        onNavigateToRubberScreen = onNavigateToRubberScreen,
                        onNavigateToCreateStickerScreen = onNavigateToCreateStickerScreen,
                        onRemoveBackground = {
                            viewModel.onAction(EditingAction.RemoveBackground)
                        },
                        onDelete = {
                            viewModel.onAction(EditingAction.DeleteImage)
                        }
                    )
                }

            }

        }
    }

}