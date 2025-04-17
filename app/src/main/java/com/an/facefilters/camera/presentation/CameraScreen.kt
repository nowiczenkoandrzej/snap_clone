package com.an.facefilters.camera.presentation

import android.graphics.PointF
import android.os.Build
import android.util.Size
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionSelector.AllowedResolutionMode
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.an.facefilters.camera.domain.CameraScreenAction
import com.an.facefilters.camera.domain.CameraScreenEvent
import java.util.concurrent.Executors

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(
    viewmodel: CameraViewModel
) {

    val state = viewmodel
        .screenState
        .collectAsState()
        .value

    val event = viewmodel
        .event
        .collectAsState(null)
        .value


    val lifecycleOwner = LocalLifecycleOwner.current

    val context = LocalContext.current

    var isResolutionSet = remember { mutableStateOf(false) }

    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or
                        CameraController.IMAGE_ANALYSIS
            )

            val analyzer = Executors.newSingleThreadExecutor()
            setImageAnalysisAnalyzer(analyzer, { imageProxy ->

                if(!isResolutionSet.value && imageProxy.image != null) {
                    val displayMetrics = context.resources.displayMetrics

                    viewmodel.onAction(CameraScreenAction.SetCameraSettings(
                        screenWidth = displayMetrics.widthPixels,
                        screenHeight = displayMetrics.widthPixels * (4/3),
                        imageHeight = imageProxy.image!!.width,
                        imageWidth = imageProxy.image!!.height
                    ))
                    isResolutionSet.value = true
                }

                viewmodel.onAction(CameraScreenAction.ProcessImage(imageProxy))
            })
        }

    }
    LaunchedEffect(Unit) {
        controller.bindToLifecycle(lifecycleOwner)
    }

    LaunchedEffect(event) {
        when (event) {
            CameraScreenEvent.SwitchToFrontCamera -> {
                controller.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            }
            CameraScreenEvent.SwitchToBackCamera -> {
                controller.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            }
            null -> {}
        }
    }


    Column (
        modifier = Modifier
            .fillMaxSize()
    ) {
        CameraPreview(
            controller = controller,
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3 / 4F)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {
                    viewmodel.onAction(CameraScreenAction.SwitchCamera)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = null
                )
            }
            OutlinedIconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoCamera,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoFilter,
                    contentDescription = null
                )
            }
        }

    }



}
