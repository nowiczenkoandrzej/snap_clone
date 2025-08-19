package com.an.feature_stickers.di

import com.an.feature_stickers.data.StickerManagerImpl
import com.an.feature_stickers.data.SubjectDetectorImpl
import com.an.feature_stickers.domain.StickerManager
import com.an.feature_stickers.domain.SubjectDetector
import com.an.feature_stickers.domain.use_cases.CreateNewSticker
import com.an.feature_stickers.domain.use_cases.LoadStickerCategories
import com.an.feature_stickers.domain.use_cases.LoadStickersByCategory
import com.an.feature_stickers.domain.use_cases.LoadUserStickers
import com.an.feature_stickers.domain.use_cases.StickersUseCases
import com.an.feature_stickers.presentation.StickerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val stickerModule = module {
    factory { LoadStickerCategories(get()) }
    factory { LoadStickersByCategory(get()) }
    factory { LoadUserStickers(get()) }
    factory { CreateNewSticker(get(), get(), get()) }

    factory<SubjectDetector> { SubjectDetectorImpl() }

    factory { StickersUseCases(get(), get(), get(), get()) }

    single<StickerManager> { StickerManagerImpl(get()) }

    viewModel { StickerViewModel(get(), get()) }
}