package com.an.facefilters.camera.presentation


import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import com.an.facefilters.camera.domain.Face


fun FaceDrawer(
    scope: DrawScope,
    scale: Float,
    delta: Float,
    face: Face
) {

    scope.clipRect {

        face.faceOval.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Blue,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.leftEyebrowTop.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Red,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.leftEyebrowBottom.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Blue,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.rightEyebrowTop.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Red,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.rightEyebrowBottom.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Blue,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.leftEye.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Green,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.rightEye.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Green,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.upperLipBottom.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Yellow,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.lowerLipTop.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Green,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.upperLipTop.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Black,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.lowerLipBottom.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Blue,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.noseBridge.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Cyan,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.noseBottom.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Magenta,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.rightCheek.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Black,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
        face.leftCheek.forEach { point ->
            val x = ((480f - point.x) * scale) - delta
            drawCircle(
                color = Color.Black,
                radius = 5f,
                center = Offset(
                    x = x,
                    y = point.y * scale
                )
            )
        }
    }

}