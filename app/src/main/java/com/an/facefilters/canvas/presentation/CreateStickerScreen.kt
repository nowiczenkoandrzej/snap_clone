package com.an.facefilters.canvas.presentation

import android.widget.ImageView
import android.widget.Toast
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.PathData
import com.an.facefilters.canvas.presentation.util.drawPath

@Composable
fun CreateStickerScreen(
    viewModel: CanvasViewModel,
    navController: NavController
) {

    val state = viewModel
        .screenState
        .collectAsState()
        .value

    val event = viewModel
        .events
        .collectAsState(null)
        .value

    val originalBitmap = (state.elements[state.selectedElementIndex!!] as Img).bitmap

    val context = LocalContext.current

    LaunchedEffect(event) {
        when(event) {
            CanvasEvent.StickerCreated -> navController.popBackStack()
            is CanvasEvent.ShowToast -> Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()

            else -> {}
        }
    }

    var currentPath by remember {
        mutableStateOf(PathData(
            color = Color.White.copy(alpha = 0.85f),
            path = emptyList(),
            thickness = 200f
        ))
    }

    var imageSize by remember { mutableStateOf(IntSize.Zero) }


    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {

        Box(
            modifier = Modifier
                .aspectRatio(3f/4f)
                .weight(4f)
        ) {
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        setImageBitmap(originalBitmap)
                    }
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .aspectRatio(originalBitmap.width.toFloat() / originalBitmap.height)
                    .pointerInput(Unit) {
                        /*detectDragGestures { change, dragAmount ->
                            currentPath = currentPath.copy(
                                path = currentPath.path + Offset(
                                    x = change.position.x,
                                    y = change.position.y
                                )
                            )
                        }*/
                        detectDragGestures(
                            onDrag = { change, _ ->
                                currentPath = currentPath.copy(
                                    path = currentPath.path + Offset(
                                        x = change.position.x,
                                        y = change.position.y
                                    )
                                )
                            },
                            onDragEnd = {

                                val left = currentPath.path.minOfOrNull { it.x }
                                val right = currentPath.path.maxOfOrNull { it.x }
                                val top = currentPath.path.minOfOrNull { it.y }
                                val bottom = currentPath.path.maxOfOrNull { it.y }

                                if(left != null && right != null && top != null && bottom != null) {
                                    val rect = Rect(
                                        top = top.toFloat(),
                                        left = left.toFloat(),
                                        right = right.toFloat(),
                                        bottom = bottom.toFloat()
                                    )


                                    val croppedBitmap = originalBitmap.cropToRect(
                                        srcRect = rect,
                                        viewSize = imageSize
                                    )
                                    viewModel.onAction(ElementAction.CreateSticker(croppedBitmap))
                                }


                            }
                        )
                    }
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            clipRect {
                                drawPath(
                                    path = currentPath.path,
                                    color = currentPath.color,
                                    thickness = currentPath.thickness
                                )
                            }
                        }
                    }
                    .onGloballyPositioned {
                        imageSize = it.size
                    }
            )
        }
    }



}