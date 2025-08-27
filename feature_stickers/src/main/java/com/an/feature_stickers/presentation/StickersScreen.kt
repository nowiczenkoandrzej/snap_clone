package com.an.feature_stickers.presentation

import android.util.Log
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

import coil.compose.AsyncImage
import com.an.core_ui.ui.theme.spacing
import kotlinx.coroutines.launch
import java.io.File

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

    val pagerState = rememberPagerState(
        initialPage = stickersState.selectedCategoryIndex,
        pageCount = { stickersState.categories.size },

    )

    BackHandler { popBackStack() }

    LaunchedEffect(pagerState.currentPage) {
        viewModel.onAction(StickerAction.SelectCategory(pagerState.currentPage))
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
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 16.dp
            ) {

                stickersState.categories.forEachIndexed { index, category ->

                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(category) }
                    )
                }

            }


            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState)
            ) { pageIndex ->
                val category = stickersState.categories[pageIndex]

                if(category == "Yours") {
                    stickersState.stickersMap["Yours"]?.let { stickers ->
                        StickerGrid(
                            stickers = stickers,
                            isFromAssets = false,
                            onStickerClick = { sticker ->
                                viewModel.onAction(
                                    StickerAction.AddSticker(sticker, false)
                                )
                            }
                        )
                    }
                } else {
                    stickersState.stickersMap[category]?.let { stickers ->
                        StickerGrid(
                            stickers = stickers.map { "stickers/${category.lowercase()}/$it" },
                            isFromAssets = true,
                            onStickerClick = { sticker ->
                                viewModel.onAction(
                                    StickerAction.AddSticker(sticker, true)
                                )
                            }
                        )
                    }
                }
            }

        }
    }


}