package com.an.facefilters.canvas.presentation.screen

import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.an.facefilters.canvas.presentation.CanvasEvent
import com.an.facefilters.canvas.presentation.CanvasViewModel
import com.an.facefilters.canvas.presentation.DrawingAction
import com.an.facefilters.canvas.presentation.UiAction
import com.an.facefilters.canvas.presentation.util.drawPencil

@Composable
fun RubberScreen(
    viewModel: CanvasViewModel,
    navController: NavController
) {

    val drawingState = viewModel
        .drawingState
        .collectAsState()
        .value

    val uiState = viewModel
        .uiState
        .collectAsState()
        .value

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                is CanvasEvent.PopBackStack -> navController.popBackStack()
                else -> {}
            }
        }
    }
    val editedBitmap = drawingState.editedImg?.bitmap

    val density = LocalDensity.current


    BackHandler { viewModel.onAction(DrawingAction.Cancel) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        editedBitmap?.let {
            Box(
                modifier = Modifier.weight(4f)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center

                ) {
                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                scaleType = ImageView.ScaleType.FIT_CENTER
                                setImageBitmap(editedBitmap)
                            }

                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .size(
                                width = with(density) { editedBitmap.width.toDp() },
                                height = with(density) { editedBitmap.height.toDp() }
                            )
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDrag = { change, _ ->
                                        viewModel.onAction(
                                            DrawingAction.UpdateCurrentPath(
                                                Offset(
                                                    x = change.position.x,
                                                    y = change.position.y
                                                )
                                            )
                                        )

                                    },
                                    onDragEnd = {
                                        viewModel.onAction(DrawingAction.AddNewPath)
                                    }
                                )
                            }
                            .drawWithCache {
                                onDrawWithContent {
                                    drawContent()
                                    clipRect {

                                        drawingState.paths.forEach { path ->
                                            drawPencil(
                                                path = path.path,
                                                color = Color.White,
                                                thickness = path.thickness
                                            )
                                        }

                                        drawPencil(
                                            path = drawingState.currentPath.path,
                                            color = Color.White,
                                            thickness = drawingState.currentPath.thickness
                                        )
                                    }
                                }
                            }

                    )

                }

            }



        }
    }



}