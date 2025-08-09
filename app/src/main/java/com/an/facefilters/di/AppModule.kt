package com.an.facefilters.di


import com.an.facefilters.canvas.data.BitmapCache
import com.an.facefilters.canvas.data.PngFileManagerImpl
import com.an.facefilters.canvas.data.StickerManagerImpl
import com.an.facefilters.canvas.data.SubjectDetectorImpl
import com.an.facefilters.canvas.domain.SubjectDetector
import com.an.facefilters.canvas.presentation.ElementAction
import com.an.facefilters.canvas.domain.managers.PngFileManager
import com.an.facefilters.canvas.domain.managers.StickerManager
import com.an.facefilters.canvas.presentation.CanvasViewModel
import com.an.facefilters.home.data.SettingsDataStore
import com.an.facefilters.home.presentation.HomeViewModel
import kotlinx.coroutines.channels.Channel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<SubjectDetector> { SubjectDetectorImpl() }

    single { SettingsDataStore(get()) }

    single { BitmapCache }

    single<StickerManager> { StickerManagerImpl(get(), get()) }
    single<PngFileManager> { PngFileManagerImpl(get()) }

    viewModel { HomeViewModel() }
    viewModel { CanvasViewModel(get(), get(), get()) }

    single {
        Channel<ElementAction>(Channel.BUFFERED)
    }
}