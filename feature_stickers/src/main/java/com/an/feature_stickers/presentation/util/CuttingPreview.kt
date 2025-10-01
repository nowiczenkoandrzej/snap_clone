package com.an.feature_stickers.presentation.util

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.an.feature_stickers.R

@Composable
fun CuttingPreview(
    resultBitmap: Bitmap,
    alpha: Float,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(5f)
        ) {
            CheckerboardBackground(
                modifier = Modifier.fillMaxSize()
            )
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_INSIDE
                        adjustViewBounds = true
                        setImageBitmap(resultBitmap)
                    }
                },
                update = { imageView ->
                    imageView.setImageBitmap(resultBitmap)
                },
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = alpha)

            )
        }

        Row(
            modifier = Modifier
                .weight(2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(
                onClick = onCancel
            ) {
                Text(stringResource(R.string.cancel))
            }
            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                onClick = onSave
            ) {
                Text(stringResource(R.string.save))
            }
        }




    }

}

@Composable
fun CheckerboardBackground(
    modifier: Modifier = Modifier,
    color1: Color = Color.LightGray,
    color2: Color = Color.White,
    cellSize: Float = 40f // px wielkość kratki
) {
    Canvas(modifier = modifier) {
        val rows = (size.height / cellSize).toInt() + 1
        val cols = (size.width / cellSize).toInt() + 1

        for (row in 0..rows) {
            for (col in 0..cols) {
                drawRect(
                    color = if ((row + col) % 2 == 0) color1 else color2,
                    topLeft = Offset(col * cellSize, row * cellSize),
                    size = androidx.compose.ui.geometry.Size(cellSize, cellSize)
                )
            }
        }
    }
}