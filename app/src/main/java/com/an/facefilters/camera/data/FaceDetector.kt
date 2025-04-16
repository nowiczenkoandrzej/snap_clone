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


    @RequiresApi(Build.VERSION_CODES.R)
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
                                leftEyebrowTop = landmarks[FaceContour.LEFT_EYEBROW_TOP].points,
                                leftEyebrowBottom = landmarks[FaceContour.LEFT_EYEBROW_BOTTOM].points,
                                rightEyebrowTop = landmarks[FaceContour.RIGHT_EYEBROW_TOP].points,
                                rightEyebrowBottom = landmarks[FaceContour.RIGHT_EYEBROW_BOTTOM].points,
                                leftEye = landmarks[FaceContour.LEFT_EYE].points,
                                rightEye = landmarks[FaceContour.RIGHT_EYE].points,
                                upperLipBottom = landmarks[FaceContour.UPPER_LIP_BOTTOM].points,
                                lowerLipTop = landmarks[FaceContour.LOWER_LIP_TOP].points,
                                upperLipTop = landmarks[FaceContour.UPPER_LIP_TOP].points,
                                lowerLipBottom = landmarks[FaceContour.LOWER_LIP_BOTTOM].points,
                                noseBridge = landmarks[FaceContour.NOSE_BRIDGE].points,
                                noseBottom = landmarks[FaceContour.NOSE_BOTTOM].points,
                                leftCheek = landmarks[FaceContour.LEFT_CHEEK].points,
                                //rightCheek = landmarks[FaceContour.RIGHT_CHEEK].points,
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