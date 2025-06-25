package com.an.facefilters.canvas.presentation

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.an.facefilters.R
import com.an.facefilters.canvas.domain.CanvasAction
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.StickerAction
import com.an.facefilters.canvas.domain.StickerCategory
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

    val event = viewModel
        .events
        .collectAsState(null)
        .value

    val stickersState = viewModel
        .stickersState
        .collectAsState()
        .value

    //var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(event) {
        when(event) {
            is CanvasEvent.StickerAdded -> {
                navController.popBackStack()
            }
            else -> {}
        }
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ScrollableTabRow(
            selectedTabIndex = stickersState.categories.indexOf(stickersState.selectedCategory.toString().toLowerCase()),
            edgePadding = 16.dp
        ) {
            stickersState.categories.forEachIndexed { index, category ->
                Tab(
                    selected = stickersState.categories.indexOf(stickersState.selectedCategory.toString().toLowerCase()) == index,
                    onClick = {
                        val newCategory = StickerCategory.values()
                            .firstOrNull { it.name.equals(category, ignoreCase = true) }
                            ?: StickerCategory.EMOJIS

                        viewModel.onAction(StickerAction.LoadStickers(newCategory))
                    },
                    text = { Text(category) }
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            Log.d("TAG", "StickersScreen: ${stickersState.stickers}")
            items(stickersState.stickers, key = { it }) { sticker ->

                val path = "file:///android_asset/stickers/${stickersState.selectedCategory.name.lowercase()}/$sticker"

                Log.d("TAG", "StickersScreen: $path")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(MaterialTheme.spacing.small),
                    contentAlignment = Alignment.Center
                ) {
                    var isLoading by remember(sticker) { mutableStateOf(true) }

                    val model = remember(sticker) { path }
                    AsyncImage(
                        model = model,
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                CoroutineScope(Dispatchers.IO).launch {
                                    withContext(Dispatchers.Main) {
                                        val filePath = "stickers/${stickersState.selectedCategory.name.lowercase()}/$sticker"
                                        viewModel.onAction(StickerAction.AddSticker(filePath))
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




}