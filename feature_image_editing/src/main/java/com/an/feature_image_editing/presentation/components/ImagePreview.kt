package com.an.feature_image_editing.presentation.components

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ImagePreview(
    bitmap: Bitmap,
    alpha: Float = 1f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        CheckerboardBackground(
            modifier = Modifier.fillMaxSize()
        )
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {


            val maxH = constraints.maxHeight.toFloat()
            val maxW = constraints.maxWidth.toFloat()
            val bitmapRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

            var targetHeight: Float
            var targetWidth: Float
            if (maxW / bitmapRatio <= maxH) {
                targetWidth = maxW
                targetHeight = maxW / bitmapRatio
            } else {
                targetHeight = maxH
                targetWidth = maxH * bitmapRatio
            }
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
                modifier = modifier
                    .width(targetWidth.dp)
                    .height(targetHeight.dp)
                    .graphicsLayer(alpha = alpha)

            )
        }
    }



}