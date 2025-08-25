package com.an.feature_stickers.presentation

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.an.core_ui.ui.theme.spacing
import com.an.feature_stickers.domain.StickerCategory
import kotlinx.coroutines.launch

@Composable
fun StickersScreen(
    viewModel: StickerViewModel,
    popBackStack: () -> Unit
) {

    val stickersState = viewModel
        .stickerState
        .collectAsState()
        .value

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var currentIndex by remember {
        mutableStateOf(1)
    }

    BackHandler {
        popBackStack()
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                StickerEvent.PopBackStack -> {
                    popBackStack()
                }
                is StickerEvent.ShowSnackbar -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            ScrollableTabRow(
                selectedTabIndex = currentIndex,
                edgePadding = 16.dp
            ) {
                stickersState.categories.forEachIndexed { index, category ->

                    val isSelected = currentIndex == index

                    Tab(
                        selected = isSelected,
                        onClick = {
                            val newCategory = StickerCategory.values()
                                .firstOrNull { it.name.equals(category, ignoreCase = true) }
                                ?: StickerCategory.ACTIVITIES

                            viewModel.onAction(StickerAction.LoadStickersByCategory(newCategory))
                            currentIndex = index
                        },
                        text = { Text(category) }
                    )
                }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
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

                                        viewModel.onAction(StickerAction.AddSticker(sticker))
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

                                        val filePath = "stickers/${stickersState.selectedCategory.name.lowercase()}/$sticker"
                                        viewModel.onAction(StickerAction.AddSticker(filePath))
                                    },
                                onSuccess = { isLoading = false },
                                onError = { isLoading = false }
                            )

                        }

                    }
                }
            }
        }
    }


}