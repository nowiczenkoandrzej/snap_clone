package com.an.feature_image_editing.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.an.feature_image_editing.R
import com.an.feature_image_editing.presentation.EditingEvent
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.RubberAction
import com.an.feature_image_editing.presentation.components.RubberArea
import com.an.feature_image_editing.presentation.components.SizeSlider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RubberScreen(
    viewModel: ImageEditingViewModel,
    popBackStack: () -> Unit
) {

    val editedBitmap = viewModel
        .editedImageModel
        .collectAsState()
        .value
        ?.bitmap

    val state = viewModel
        .rubberState
        .collectAsState()
        .value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()




    BackHandler {
        viewModel.onAction(RubberAction.Cancel)
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

        editedBitmap?.let {

            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val displayedBitmap = if(state.changesStack.isEmpty()) {
                    editedBitmap
                } else {
                    state.changesStack.last()
                }
                val alpha = viewModel
                    .editedImageModel
                    .value
                    ?.alpha ?: 1f
                RubberArea(
                    modifier = Modifier
                        .weight(5f),
                    displayedBitmap = displayedBitmap,
                    alpha = alpha,
                    currentPath = state.currentPath,
                    onDrag = { offset, scale ->
                        viewModel.onAction(RubberAction.UpdateCurrentPath(offset, scale))
                    },
                    onDragEnd = {
                        viewModel.onAction(RubberAction.AddNewPath)
                    }
                )

                Column(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    SizeSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        onValueChange = { newThickness ->
                            viewModel.onAction(RubberAction.SelectThickness(newThickness))
                        },
                        value = state.pathThickness,
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                viewModel.onAction(RubberAction.Cancel)
                            }
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                        TextButton(
                            onClick = {
                                viewModel.onAction(RubberAction.SaveRubber)

                            }
                        ) {
                            Text(stringResource(R.string.save))
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(
                            onClick = {
                                viewModel.onAction(RubberAction.UndoPath)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Undo,
                                contentDescription = null
                            )
                        }
                    }


                }
            }

        }
    }


}