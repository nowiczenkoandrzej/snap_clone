package com.an.facefilters.canvas.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest

@Composable
fun SvgSticker(
    modifier: Modifier = Modifier,
    svgAsset: String
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data("file:///android_asset/sticker/$svgAsset")
            .build(),
        imageLoader = imageLoader,
        contentDescription = null,
        modifier = modifier
    )
}