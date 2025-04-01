package com.an.facefilters

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.an.facefilters.ui.theme.FaceFiltersTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.util.concurrent.Executors

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this,
                CAMERAX_PERMISSIONS,
                0
            )
        }

        val opts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()





        enableEdgeToEdge()
        setContent {
            FaceFiltersTheme {
                val lifecycleOwner = LocalLifecycleOwner.current

                val faceDetector = remember { FaceDetection.getClient(opts) }


                var contours = remember { mutableStateOf(emptyList<PointF>()) }

                val scaffoldState = rememberBottomSheetScaffoldState()
                val controller = remember {
                    LifecycleCameraController(applicationContext).apply {
                        setEnabledUseCases(
                            CameraController.IMAGE_CAPTURE or
                            CameraController.IMAGE_ANALYSIS
                        )
                        val analyzer = Executors.newSingleThreadExecutor()
                        setImageAnalysisAnalyzer(analyzer, { imageProxy ->
                            processImageProxy(
                                faceDetector = faceDetector,
                                imageProxy = imageProxy,
                                onFaceDetected = { points ->
                                    contours.value = points
                                    Log.d("TAG", "onCreate: contours: ${contours.value}")
                                }
                            )
                        })
                    }



                }
                LaunchedEffect(Unit) {
                    controller.bindToLifecycle(lifecycleOwner)
                }


                Box(
                    modifier = Modifier.fillMaxSize()
                ) {
                    AndroidView(
                        factory = { context ->
                            PreviewView(context).apply {
                                this.controller = controller
                                scaleType = PreviewView.ScaleType.FILL_CENTER
                            }
                        },
                        modifier = Modifier
                            .fillMaxSize()

                    )
                    Canvas(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        contours.value.forEach { point ->
                            Log.d("TAG", "onCreate: $point")
                            drawCircle(
                                color = Color.Blue,
                                radius = 5f,
                                center = Offset(x = point.x, y = point.y)
                            )
                        }
                    }


                    IconButton(
                        onClick = {
                            controller.cameraSelector =
                                if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                    CameraSelector.DEFAULT_FRONT_CAMERA
                                } else CameraSelector.DEFAULT_BACK_CAMERA


                        },
                        modifier = Modifier
                            .offset(16.dp, 32.dp)

                    ) {
                        Icon(
                            imageVector = Icons.Default.Cameraswitch,
                            contentDescription = null
                        )
                    }
                }


            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(
        faceDetector: FaceDetector,
        imageProxy: ImageProxy,
        onFaceDetected: (List<PointF>) -> Unit
    ) {
        val mediaImage = imageProxy.image
        if(mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    //Log.d("TAG", "processImageProxy: faces: $faces")
                    if(faces.isNotEmpty()) {
                        val point = faces[0].allContours[0].points
                        Log.d("TAG", "processImageProxy: $point")
                        onFaceDetected(point)
                    }
                }
                .addOnFailureListener { e ->

                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}
