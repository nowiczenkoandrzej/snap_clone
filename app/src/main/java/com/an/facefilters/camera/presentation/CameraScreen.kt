package com.an.facefilters.camera.presentation

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.an.facefilters.camera.domain.CameraScreenAction
import com.an.facefilters.camera.domain.CameraScreenEvent
import com.an.facefilters.core.Screen
import java.util.concurrent.Executors
import androidx.core.graphics.scale

@OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    navController: NavController,
) {

    val state = viewModel
        .screenState
        .collectAsState()
        .value

    val event = viewModel
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
            val displayMetrics = context.resources.displayMetrics


            val analyzer = Executors.newSingleThreadExecutor()
            setImageAnalysisAnalyzer(analyzer, { imageProxy ->

                if(!isResolutionSet.value && imageProxy.image != null) {


                    viewModel.onAction(CameraScreenAction.SetCameraSettings(
                        screenWidth = displayMetrics.widthPixels,
                        screenHeight = displayMetrics.widthPixels * (4/3),
                        imageHeight = imageProxy.image!!.width,
                        imageWidth = imageProxy.image!!.height
                    ))
                    isResolutionSet.value = true
                }

                viewModel.onAction(CameraScreenAction.ProcessImage(imageProxy))
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
            CameraScreenEvent.NavigateToCanvas -> {
                navController.navigate(Screen.Canvas.route)
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
                    viewModel.onAction(CameraScreenAction.SwitchCamera)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = null
                )
            }
            OutlinedIconButton(
                onClick = {
                    takePhoto(
                        controller = controller,
                        context = context,
                        screenWidth = state.screenWidth,
                        onPhotoTaken = { bitmap ->

                            navController.currentBackStackEntry?.savedStateHandle?.set(
                                "photo",
                                bitmap
                            )

                            viewModel.onAction(CameraScreenAction.TakePhoto)
                        }
                    )

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
fun takePhoto(
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit,
    context: Context,
    screenWidth: Int,
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object: OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)

                val originalBitmap = image.toBitmap()

                val scale = (screenWidth.toFloat() / originalBitmap.width)

                val reducedBitmap = originalBitmap.scale(
                    (originalBitmap.width * scale).toInt(),
                    (originalBitmap.height * scale).toInt()
                )

                onPhotoTaken(reducedBitmap)

            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)

                Log.d("TAG", "onError: ${exception.message}")
            }
        }
    )
}

