package com.an.feature_drawing.presentation.components

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.an.feature_drawing.presentation.util.drawPencil
import kotlin.math.min
import kotlin.math.roundToInt

@Composable
fun Magnifier(
    modifier: Modifier = Modifier,
    bitmap: Bitmap,
    fingerPosition: Offset,
    alpha: Float,
    currentPath: List<Offset>
) {

    val magnifierSize = 148.dp
    val magnification = 0.9f

    val pathSegments = mutableListOf<MutableList<Offset>>()
    var currentSegment = mutableListOf<Offset>()

    BoxWithConstraints(
        modifier = modifier
    ) {
        val maxH = constraints.maxHeight.toFloat()
        val maxW = constraints.maxWidth.toFloat()

        val scale = min(
            maxW / bitmap.width.toFloat(),
            maxH / bitmap.height.toFloat()
        )

        var targetHeight = bitmap.height * scale
        var targetWidth = bitmap.width * scale

        val offsetX = (maxW - targetWidth) / 2f
        val offsetY = (maxH - targetHeight) / 2f

        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.CENTER_INSIDE
                    adjustViewBounds = true
                    setImageBitmap(bitmap)
                }
            },
            update = { imageView ->
                imageView.setImageBitmap(bitmap)
            },
            modifier = Modifier
                .width(targetWidth.dp)
                .height(targetHeight.dp)
                .graphicsLayer(alpha = alpha)


        )
        fingerPosition.let { pos ->

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
                        image = bitmap.asImageBitmap(),
                        srcOffset = srcOffset,
                        srcSize = srcSize,
                        dstSize = IntSize(size.width.toInt(), size.height.toInt())
                    )
                    val scaleX = size.width / srcSize.width.toFloat()
                    val scaleY = size.height / srcSize.height.toFloat()

                    currentPath.forEach { p ->
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
}