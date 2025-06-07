package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
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
import com.an.facefilters.canvas.presentation.util.drawPencil
import androidx.core.graphics.createBitmap

@Composable
fun CreateStickerScreen(
    viewModel: CanvasViewModel,
    navController: NavController,
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

    val thickness = 200f

    var currentPath by remember {
        mutableStateOf(PathData(
            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.85f),
            path = emptyList(),
            thickness = thickness
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
                                        top = top.toFloat() - thickness / 2,
                                        left = left.toFloat() - thickness / 2,
                                        right = right.toFloat() + thickness / 2,
                                        bottom = bottom.toFloat() + thickness / 2
                                    )



                                    val path = Path().apply {
                                        moveTo(currentPath.path[0].x, currentPath.path[0].y)
                                        currentPath.path.forEach {
                                            lineTo(it.x, it.y)
                                        }
                                    }

                                    val result = extractSelectedArea(
                                        path = path,
                                        originalBitmap = originalBitmap,
                                        strokeWidth = thickness
                                    ).cropToRect(rect, imageSize)
                                    viewModel.onAction(ElementAction.CreateSticker(result))
                                }


                            }
                        )
                    }
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            clipRect {
                                drawPencil(
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

private fun extractSelectedArea(
    path: Path,
    originalBitmap: Bitmap,
    strokeWidth: Float,
): Bitmap  {

    val softwareBitmap = if(originalBitmap.config == Bitmap.Config.HARDWARE) {
        originalBitmap.copy(Bitmap.Config.ARGB_8888, false)
    } else {
        originalBitmap
    }

    val output = createBitmap(softwareBitmap.width, softwareBitmap.height)

    val canvas = Canvas(output)

    val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        this.strokeWidth = strokeWidth
        color = Color.BLACK
    }

    canvas.drawPath(path, paint)

    val resultPaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }


    canvas.drawBitmap(softwareBitmap, 0f, 0f, resultPaint)
    return output

}