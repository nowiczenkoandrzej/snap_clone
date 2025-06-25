package com.an.facefilters.canvas.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.an.facefilters.R
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.StickerManager
import com.an.facefilters.canvas.presentation.util.loadPngAssetAsImageBitmap
import com.an.facefilters.canvas.presentation.util.loadStickerFileNames
import com.an.facefilters.ui.theme.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun StickersScreen(
    navController: NavController,
    viewModel: CanvasViewModel,
) {
    val context = LocalContext.current
    val stickers1 = loadStickerFileNames(context)
    val stickers2 = loadStickerFileNames(context)

    val stickers = stickers1 + stickers2

    val event = viewModel
        .events
        .collectAsState(null)
        .value

    LaunchedEffect(event) {
        when(event) {
            is CanvasEvent.StickerAdded -> {
                navController.popBackStack()
            }
            else -> {}
        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3)
    ) {
        items(stickers, key = { it }) { sticker ->

            val assetPath = "file:///android_asset/stickers/$sticker"


            val painter = rememberAsyncImagePainter(
                model = assetPath
            )

            val state = painter.state

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(MaterialTheme.spacing.small),
                contentAlignment = Alignment.Center
            ) {
                var isLoading by remember(sticker) { mutableStateOf(true) }

                val model = remember(sticker) { assetPath }
                AsyncImage(
                    model = model,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            CoroutineScope(Dispatchers.IO).launch {
                                val bitmap = loadPngAssetAsImageBitmap(context, sticker)
                                withContext(Dispatchers.Main) {
                                    viewModel.onAction(ElementAction.AddSticker(bitmap))
                                }
                            }
                        },
                    onSuccess = { isLoading = false },
                    onError = { isLoading = false }
                )

                if (isLoading) {
                    CircularProgressIndicator()
                }
            }

        }
    }


}