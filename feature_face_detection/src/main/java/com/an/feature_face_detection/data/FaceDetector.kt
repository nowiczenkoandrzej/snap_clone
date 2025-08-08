package com.an.facefilters.camera.data

import android.graphics.PointF
import android.os.Build
import android.util.Log
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.an.facefilters.camera.domain.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour

class FaceDetector {

    private val opts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        //.setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .build()

    private val faceDetector = FaceDetection.getClient(opts)


    @OptIn(ExperimentalGetImage::class)
    fun processImageProxy(
        imageProxy: ImageProxy,
        onFaceDetected: (Face) -> Unit,
    ) {
        val mediaImage = imageProxy.image
        if(mediaImage != null) {
            val image = InputImage.fromMediaImage(
                mediaImage,
                imageProxy.imageInfo.rotationDegrees
            )

            faceDetector.process(image)
                .addOnSuccessListener { faces ->
                    if(faces.isNotEmpty()) {
                        val landmarks = faces[0].allContours


                        val face = if(landmarks.isNotEmpty()) {
                            Face(
                                faceOval = landmarks[0].points,
                                leftEyebrowTop = landmarks[1].points,
                                leftEyebrowBottom = landmarks[2].points,
                                rightEyebrowTop = landmarks[3].points,
                                rightEyebrowBottom = landmarks[4].points,
                                leftEye = landmarks[5].points,
                                rightEye = landmarks[6].points,
                                upperLipBottom = landmarks[8].points,
                                lowerLipTop = landmarks[9].points,
                                upperLipTop = landmarks[7].points,
                                lowerLipBottom = landmarks[10].points,
                                noseBridge = landmarks[11].points,
                                noseBottom = landmarks[12].points,
                                leftCheek = landmarks[13].points,
                                rightCheek = landmarks[14].points,
                            )
                        } else {
                            Face()
                        }

                        onFaceDetected(face)
                    } else {
                        onFaceDetected(Face())
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
}