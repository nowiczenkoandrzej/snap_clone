package com.an.facefilters.di

import com.an.facefilters.camera.data.FaceDetector
import com.an.facefilters.camera.presentation.CameraViewModel
import com.an.facefilters.canvas.presentation.CanvasViewModel
import com.an.facefilters.core.permissions.PermissionManager
import com.an.facefilters.home.data.SettingsDataStore
import com.an.facefilters.home.presentation.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {


    single { FaceDetector() }
    single { SettingsDataStore(get()) }
    single { PermissionManager(get())}

    viewModel { CameraViewModel(get()) }
    viewModel { HomeViewModel() }
    viewModel { CanvasViewModel() }
}