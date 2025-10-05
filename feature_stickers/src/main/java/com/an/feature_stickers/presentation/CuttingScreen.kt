package com.an.feature_stickers.presentation

import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.an.feature_stickers.R
import com.an.feature_stickers.presentation.util.CuttingPreview
import com.an.feature_stickers.presentation.util.drawPencil
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun CuttingScreen(
    viewModel: StickerViewModel,
    popBackStack: () -> Unit
) {

    val editedBitmap = viewModel
        .editedImageModel
        .collectAsState()
        .value
        ?.bitmap

    val state = viewModel
        .cuttingState
        .collectAsState()
        .value

    var fingerPosition by remember { mutableStateOf<Offset?>(null) }

    val magnifierSize = 148.dp
    val magnification = 0.9f



    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BackHandler { popBackStack() }



    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                StickerEvent.PopBackStack -> {
                    popBackStack()
                }
                is StickerEvent.ShowSnackbar -> scope.launch {
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
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                BoxWithConstraints(
                    modifier = Modifier
                        .weight(1f)
                ) {

                    val maxH = constraints.maxHeight.toFloat()
                    val maxW = constraints.maxWidth.toFloat()

                    val scale = min(
                        maxW / editedBitmap.width.toFloat(),
                        maxH / editedBitmap.height.toFloat()
                    )


                    var targetHeight = editedBitmap.height * scale
                    var targetWidth = editedBitmap.width * scale


                    val offsetX = (maxW - targetWidth) / 2f
                    val offsetY = (maxH - targetHeight) / 2f


                    val alpha = viewModel
                        .editedImageModel
                        .value
                        ?.alpha ?: 1f

                    AndroidView(
                        factory = { context ->
                            ImageView(context).apply {
                                scaleType = ImageView.ScaleType.CENTER_INSIDE
                                adjustViewBounds = true
                                setImageBitmap(editedBitmap)
                            }
                        },
                        update = { imageView ->
                            imageView.setImageBitmap(editedBitmap)
                        },
                        modifier = Modifier
                            .width(targetWidth.dp)
                            .height(targetHeight.dp)
                            .graphicsLayer(alpha = alpha)


                    )

                    Canvas(
                        modifier = Modifier
                            .width(targetWidth.dp)
                            .height(targetHeight.dp)
                            .graphicsLayer {
                                translationX = offsetX
                                translationY = offsetY
                            }
                            .pointerInput(state.showMagnifier) {
                                detectDragGestures(
                                    onDragStart = { start ->
                                        if(state.showMagnifier) {
                                            fingerPosition = start
                                        }
                                    },
                                    onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                        val localX = (change.position.x) / scale
                                        val localY = (change.position.y) / scale

                                        if(state.showMagnifier) {
                                            fingerPosition = change.position
                                        }

                                        viewModel.onAction(
                                            StickerAction.UpdateCurrentPath(
                                                Offset(
                                                    localX,
                                                    localY
                                                )
                                            )
                                        )
                                    },
                                    onDragEnd = {

                                        viewModel.onAction(StickerAction.CutBitmap)
                                        fingerPosition = null
                                    }
                                )
                            }
                    ){

                        clipRect {
                            drawPencil(
                                path = state.currentPath.map { p ->
                                    Offset(p.x * scale, p.y * scale)
                                },
                                color = Color.Black.copy(alpha = 0.7f),
                                thickness = 8f
                            )
                        }

                    }

                    fingerPosition?.let { pos ->
                        val pathSegments = mutableListOf<MutableList<Offset>>()
                        var currentSegment = mutableListOf<Offset>()
                        Box(
                            modifier = Modifier
                                .size(magnifierSize)
                                .offset {
                                    IntOffset(
                                        x = (offsetX + pos.x).roundToInt() - magnifierSize.roundToPx() / 2,
                                        y = (offsetY + pos.y).roundToInt() - 420 // above finger
                                    )
                                }
                                .background(Color.White, shape = CircleShape)
                                .border(2.dp, Color.Black, CircleShape)
                                .clip(CircleShape)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val srcSize = IntSize(
                                    (magnifierSize.toPx() / magnification / scale).toInt() ,
                                    (magnifierSize.toPx() / magnification / scale).toInt()
                                )
                                val srcOffset = IntOffset(
                                    (pos.x / scale - srcSize.width / 2).toInt(),
                                    (pos.y / scale - srcSize.height / 2).toInt()
                                )


                                drawImage(
                                    image = editedBitmap.asImageBitmap(),
                                    srcOffset = srcOffset,
                                    srcSize = srcSize,
                                    dstSize = IntSize(size.width.toInt(), size.height.toInt())
                                )
                                val scaleX = size.width / srcSize.width.toFloat()
                                val scaleY = size.height / srcSize.height.toFloat()

                                state.currentPath.forEach { p ->
                                    val bitmapX = p.x
                                    val bitmapY = p.y

                                    val xInSrc = bitmapX - srcOffset.x
                                    val yInSrc = bitmapY - srcOffset.y

                                    if (xInSrc in 0f..srcSize.width.toFloat() && yInSrc in 0f..srcSize.height.toFloat()) {
                                        val localX = xInSrc / srcSize.width
                                        val localY = yInSrc / srcSize.height
                                        currentSegment.add(Offset(localX * size.width, localY * size.height))
                                    } else {
                                        if (currentSegment.isNotEmpty()) {
                                            pathSegments.add(currentSegment)
                                            currentSegment = mutableListOf()
                                        }
                                    }
                                    if (currentSegment.isNotEmpty()) {
                                        pathSegments.add(currentSegment)
                                    }
                                }

                                pathSegments.forEach { segment ->
                                    if (segment.size > 1) {
                                        drawPencil(
                                            path = segment,
                                            color = Color.Black.copy(alpha = 0.8f),
                                            thickness = 8f
                                        )
                                    }
                                }
                            }
                        }
                    }



                }



                state.resultBitmap?.let {
                    CuttingPreview(
                        resultBitmap = it,
                        alpha = viewModel.editedImageModel.value?.alpha ?: 1f,
                        onSave = { viewModel.onAction(StickerAction.ConfirmCutting) },
                        onCancel = { viewModel.onAction(StickerAction.CancelCutting) }
                    )
                } ?: Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        popBackStack()
                    }) {
                        Text(stringResource(R.string.cancel))
                    }
                    IconButton(onClick = {
                        viewModel.onAction(StickerAction.ToggleMagnifier)
                    }) {

                        if(state.showMagnifier) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        }
                    }
                }

            }
        }

    }

}