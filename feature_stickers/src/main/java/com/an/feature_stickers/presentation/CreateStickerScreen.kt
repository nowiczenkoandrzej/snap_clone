package com.an.feature_stickers.presentation

import android.graphics.Bitmap
import android.graphics.Path
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.an.core_editor.presentation.toPoint
import com.an.feature_stickers.presentation.util.drawPencil
import com.an.feature_stickers.presentation.util.extractSelectedArea
import kotlinx.coroutines.launch
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun CreateStickerScreen(
    viewModel: StickerViewModel,
    popBackStack: () -> Unit
) {

    val editedBitmap = viewModel
        .editedImageModel
        .collectAsState()
        .value
        ?.bitmap

    val state = viewModel
        .createStickerState
        .collectAsState()
        .value

    var fingerPosition by remember { mutableStateOf<Offset?>(null) }

    val magnifierSize = 148.dp
    val magnification = 0.9f

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    BackHandler { popBackStack() }

    var btm by remember {
        mutableStateOf<Bitmap?>(null)
    }

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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
            ) {

                val containerWidth = constraints.maxWidth.toFloat()
                val containerHeight = constraints.maxHeight.toFloat()


                editedBitmap?.let {
                    val scale = min(
                        containerWidth / editedBitmap.width.toFloat(),
                        containerHeight / editedBitmap.height.toFloat()
                    )
                    val scaledWidth = editedBitmap.width * scale
                    val scaledHeight = editedBitmap.height * scale

                    val offsetX = (containerWidth - scaledWidth) / 2f
                    val offsetY = (containerHeight - scaledHeight) / 2f

                    Box(
                        modifier = Modifier
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .size(scaledWidth.dp, scaledHeight.dp)
                            .pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { start ->
                                        fingerPosition = start
                                    },
                                    onDrag = { change, _ ->
                                        fingerPosition = change.position
                                        viewModel.onAction(StickerAction.UpdateCurrentPath(change.position))
                                    },
                                    onDragEnd = {
                                        viewModel.onAction(StickerAction.AddPath)
                                        fingerPosition = null
                                    }
                                )
                            }
                    ) {

                        Canvas(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if(btm == null) {
                                drawImage(
                                    image = editedBitmap.asImageBitmap(),
                                    dstSize = IntSize(scaledWidth.toInt(), scaledHeight.toInt())
                                )
                            } else {
                                drawImage(
                                    image = btm!!.asImageBitmap(),
                                    dstSize = IntSize(scaledWidth.toInt(), scaledHeight.toInt())

                                )
                            }


                            state.paths.forEach { path ->
                                drawPencil(
                                    path = path,
                                    thickness = 300f,
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            }

                            drawPencil(
                                path = state.currentPath,
                                thickness = 300f,
                                color = Color.White.copy(alpha = 0.7f)
                            )




                        }

                    }

                    fingerPosition?.let { pos ->
                        Box(
                            modifier = Modifier
                                .size(magnifierSize)
                                .offset {
                                    IntOffset(
                                        x = pos.x.roundToInt() - magnifierSize.roundToPx() / 2,
                                        y = (pos.y - 160).roundToInt() // nad palcem
                                    )
                                }
                                .background(Color.White, shape = CircleShape)
                                .border(2.dp, Color.Black, CircleShape)
                                .clip(CircleShape)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                // Wycinamy fragment spod palca
                                val srcSize = IntSize(
                                    (magnifierSize.toPx() / magnification).toInt(),
                                    (magnifierSize.toPx() / magnification).toInt()
                                )
                                val srcOffset = IntOffset(
                                    (pos.x / scale - srcSize.width / 2).toInt(),
                                    (pos.y / scale - srcSize.height / 2).toInt()
                                )

                                // Powiększamy go na całe okienko
                                drawImage(
                                    image = editedBitmap.asImageBitmap(),
                                    srcOffset = srcOffset,
                                    srcSize = srcSize,
                                    dstSize = IntSize(size.width.toInt(), size.height.toInt())
                                )
                            }
                        }
                    }



                }


            }

            Row(
               modifier = Modifier
                   .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null
                    )
                }
                IconButton(onClick = {

                    val firstLine = state.paths[0]

                    val path = Path().apply {
                        moveTo(firstLine[0].x, firstLine[0].y)
                        firstLine.forEach {
                            lineTo(it.x,it.y)
                        }
                    }

                    btm = extractSelectedArea(
                        path = path,
                        originalBitmap = editedBitmap!!,
                        strokeWidth = 300f
                    )
                    //viewModel.onAction(StickerAction.CreateSticker())
                }) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null
                    )
                }
            }

        }
    }

}