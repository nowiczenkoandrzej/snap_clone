package com.an.facefilters.di

import com.an.facefilters.camera.data.FaceDetector
import com.an.facefilters.camera.presentation.CameraViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { FaceDetector() }
    viewModel { CameraViewModel(get()) }
}