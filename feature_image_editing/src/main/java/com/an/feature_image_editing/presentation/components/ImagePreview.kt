package com.an.feature_image_editing.presentation.components

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ImagePreview(
    modifier: Modifier = Modifier,
    bitmap: Bitmap
) {
    AndroidView(
        factory = { context ->
            ImageView(context).apply {
                scaleType = ImageView.ScaleType.FIT_CENTER
                setImageBitmap(bitmap)
            }
        },
        modifier = modifier

    )

}