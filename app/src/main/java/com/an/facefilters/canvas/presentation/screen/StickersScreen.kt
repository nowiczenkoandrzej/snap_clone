package com.an.facefilters.canvas.presentation.screen

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.an.facefilters.canvas.data.toBitmap
import com.an.facefilters.canvas.domain.CanvasEvent
import com.an.facefilters.canvas.domain.EditingAction
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.StickerAction
import com.an.facefilters.canvas.domain.StickerCategory
import com.an.facefilters.canvas.presentation.CanvasViewModel
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

    val event = viewModel
        .events
        .collectAsState(null)
        .value

    val stickersState = viewModel
        .stickersState
        .collectAsState()
        .value


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

                        viewModel.onAction(StickerAction.LoadStickersByCategory(newCategory))
                    },
                    text = { Text(category) }
                )
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            if(stickersState.selectedCategory == StickerCategory.YOURS) {


                items(stickersState.userStickers, key = { it }) { sticker ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(MaterialTheme.spacing.small),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = sticker,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        withContext(Dispatchers.Main) {
                                            viewModel.onAction(ElementAction.AddImage(sticker.toBitmap()))
                                        }
                                    }
                                }
                        )
                    }
                }

            } else {
                items(stickersState.stickers, key = { it }) { sticker ->

                    val path = "file:///android_asset/stickers/${stickersState.selectedCategory.name.lowercase()}/$sticker"

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




}