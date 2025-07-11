package com.an.facefilters.di

import com.an.facefilters.camera.data.FaceDetector
import com.an.facefilters.camera.presentation.CameraViewModel
import com.an.facefilters.canvas.data.PngFileManagerImpl
import com.an.facefilters.canvas.data.StickerManagerImpl
import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.domain.ElementAction
import com.an.facefilters.canvas.domain.PngFileManager
import com.an.facefilters.canvas.domain.StickerManager
import com.an.facefilters.canvas.presentation.CanvasViewModel
import com.an.facefilters.home.data.SettingsDataStore
import com.an.facefilters.home.presentation.HomeViewModel
import kotlinx.coroutines.channels.Channel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { SubjectDetector() }

    single { FaceDetector() }
    single { SettingsDataStore(get()) }

    single<StickerManager> { StickerManagerImpl(get(), get()) }
    single<PngFileManager> { PngFileManagerImpl(get()) }

    viewModel { CameraViewModel(get()) }
    viewModel { HomeViewModel() }
    viewModel { CanvasViewModel(get(), get(), get()) }

    single {
        Channel<ElementAction>(Channel.BUFFERED)
    }
}