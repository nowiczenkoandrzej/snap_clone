package com.an.facefilters.canvas.presentation

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.an.facefilters.canvas.presentation.util.loadPngAssetAsImageBitmap
import com.an.facefilters.canvas.presentation.util.loadStickerFileNames

@Composable
fun StickersScreen(
    navController: NavController,
    viewModel: CanvasViewModel
) {
    val context = LocalContext.current
    val stickers = loadStickerFileNames(context)

    LazyHorizontalGrid(
        rows = GridCells.Fixed(3)
    ) {
        items(stickers) { sticker ->
            AsyncImage(
                model = loadPngAssetAsImageBitmap(context, sticker),
                contentDescription = null
            )
        }
    }


}