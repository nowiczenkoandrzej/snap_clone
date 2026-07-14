package com.an.feature_canvas.di

import com.an.feature_canvas.domain.use_cases.AddImage
import com.an.feature_canvas.domain.use_cases.CanvasUseCases
import com.an.feature_canvas.domain.use_cases.TransformElement
import com.an.feature_canvas.domain.use_cases.Undo
import com.an.feature_canvas.presentation.CanvasViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val canvasModule = module {
    factory { AddImage( get(), get(), get() ) }
    factory { TransformElement( get() ) }
    factory { Undo( get() ) }


    factory { CanvasUseCases(get(), get()) }

    viewModel { CanvasViewModel(get(), get(), get(), get(), get()) }
}