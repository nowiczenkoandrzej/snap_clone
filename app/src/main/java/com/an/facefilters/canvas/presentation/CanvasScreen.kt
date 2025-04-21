package com.an.facefilters.canvas.presentation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.model.Img

@Composable
fun CanvasScreen(
    viewModel: CanvasViewModel,
    navController: NavController
) {


    val state = viewModel
        .screenState
        .collectAsState()
        .value

    val context = LocalContext.current

    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->

        uri?.let {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }

            val bitmap = context.contentResolver.openInputStream(uri).use { input ->
                BitmapFactory.decodeStream(input, null, options)
            } ?: return@rememberLauncherForActivityResult

            viewModel.onAction(CanvasAction.AddImage(bitmap = bitmap))

        }
    }





    LaunchedEffect(Unit) {
        val bitmap = navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<Bitmap>("photo")

        if(bitmap != null) {
            viewModel.onAction(CanvasAction.InsertInitialBitmap(bitmap))
        }

    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3 / 4F)
                .pointerInput(Unit) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->

                        val currentIndex = viewModel.screenState.value.selectedLayerIndex

                        Log.d("TAG", "CanvasScreen: index: ${state.selectedLayerIndex}")

                        if (currentIndex == null) {
                            viewModel.onAction(CanvasAction.SelectLayer(centroid))
                        } else {
                            viewModel.onAction(
                                CanvasAction.TransformLayer(
                                    scale = zoom,
                                    rotation = rotation,
                                    offset = pan
                                )
                            )
                        }

                    }
                }
        ) {

            clipRect {

                state.layers.forEach { layer ->
                    withTransform({
                        rotate(layer.rotationAngle)
                        scale(layer.scale)
                    }) {
                        when(layer) {
                            is Img -> {
                                drawImage(
                                    image = layer.bitmap.asImageBitmap(),
                                    topLeft = layer.p1
                                )
                            }
                        }
                    }
                }

            }

        }

        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly

        ) {
            TextButton(
                onClick = {

                }
            ) {
                Text("Layers")
            }
            TextButton(
                onClick = {

                }
            ) {
                Text("Tools")
            }
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }

    }

}