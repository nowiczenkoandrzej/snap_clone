package com.an.feature_image_editing.presentation.components

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.scale
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.presentation.toOffsetList
import com.an.feature_image_editing.presentation.util.drawPencil
import kotlin.math.min

@Composable
fun RubberArea(
    modifier: Modifier = Modifier,
    displayedBitmap: Bitmap,
    alpha: Float = 1f,
    onDrag: (Offset, Float) -> Unit,
    onDragEnd: () -> Unit,
    currentPath: PathData
) {
    Box(
        modifier = modifier
    ) {
        CheckerboardBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val maxH = constraints.maxHeight.toFloat()
            val maxW = constraints.maxWidth.toFloat()

            val scale = min(
                maxW / displayedBitmap.width.toFloat(),
                maxH / displayedBitmap.height.toFloat()
            )

            var targetHeight = displayedBitmap.height * scale
            var targetWidth = displayedBitmap.width * scale

            val offsetX = (maxW - targetWidth) / 2f
            val offsetY = (maxH - targetHeight) / 2f

            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                        adjustViewBounds = true
                        setImageBitmap(displayedBitmap)
                    }
                },
                update = { imageView ->
                    imageView.setImageBitmap(displayedBitmap)
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
                    .alpha(alpha)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change: PointerInputChange, dragAmount: Offset ->
                                val localX = (change.position.x) / scale
                                val localY = (change.position.y) / scale
                                onDrag(Offset(localX, localY), scale)
                            },
                            onDragEnd = {
                                onDragEnd()
                            }
                        )
                    }
            ) {


                drawIntoCanvas { canvas ->

                    drawPencil(
                        path = currentPath.path.map { p ->
                            Offset(p.x * scale, p.y * scale)
                        },
                        color = Color.White.copy(alpha = 0.5f),
                        thickness = currentPath.thickness * scale
                    )

                }
            }


        }
    }
}