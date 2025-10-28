package com.an.feature_stickers.di

import com.an.feature_stickers.data.StickerManagerImpl
import com.an.feature_stickers.domain.StickerManager
import com.an.feature_stickers.domain.use_cases.AddStickerToElements
import com.an.feature_stickers.domain.use_cases.SaveCutting
import com.an.feature_stickers.domain.use_cases.CutImage
import com.an.feature_stickers.domain.use_cases.LoadStickerCategories
import com.an.feature_stickers.domain.use_cases.LoadStickersByCategory
import com.an.feature_stickers.domain.use_cases.LoadStickersMap
import com.an.feature_stickers.domain.use_cases.StickersUseCases
import com.an.feature_stickers.presentation.StickerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val stickerModule = module {
    factory { LoadStickerCategories(get()) }
    factory { LoadStickersByCategory(get()) }
    factory { CutImage(get()) }
    factory { SaveCutting(get(), get()) }
    factory { AddStickerToElements(get()) }
    factory { LoadStickersMap(get()) }


    factory { StickersUseCases(get(), get(), get(), get()) }

    single<StickerManager> { StickerManagerImpl(get()) }

    viewModel { StickerViewModel(get(), get(), get(), get()) }
}