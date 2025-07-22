package com.an.facefilters.canvas.presentation.screen

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.model.PathData
import com.an.facefilters.canvas.presentation.CanvasViewModel
import com.an.facefilters.canvas.presentation.DrawingAction

import com.an.facefilters.canvas.presentation.util.drawPencil

@Composable
fun DrawingScreen(
    viewModel: CanvasViewModel,
    navController: NavController
) {

    val drawingState = viewModel
        .drawingState
        .collectAsState()
        .value

    var paths by remember { mutableStateOf(listOf<PathData>()) }



    val editedBitmap = drawingState.editedImg!!.bitmap

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        Box(
            modifier = Modifier
                .aspectRatio(3f / 4f)
                .weight(4f)
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
                    .fillMaxWidth()
                    .aspectRatio(editedBitmap.width.toFloat() / editedBitmap.height)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, _ ->
                                viewModel.onAction(DrawingAction.UpdateCurrentPath(Offset(
                                    x = change.position.x,
                                    y = change.position.y
                                )))

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
                                drawPencil(
                                    path = drawingState.currentPath.path,
                                    color = drawingState.currentPath.color,
                                    thickness = drawingState.currentPath.thickness
                                )
                                drawingState.paths.forEach { path ->
                                    drawPencil(
                                        path = path.path,
                                        color = path.color,
                                        thickness = path.thickness
                                    )
                                }
                            }
                        }
                    }

            )
        }


    }
}