package com.an.feature_canvas.di

import com.an.feature_canvas.data.PngFileSaverImpl
import com.an.feature_canvas.domain.PngFileSaver
import com.an.feature_canvas.domain.use_cases.AddImage
import com.an.feature_canvas.domain.use_cases.CanvasUseCases
import com.an.feature_canvas.domain.use_cases.DeleteElement
import com.an.feature_canvas.domain.use_cases.ReorderElements
import com.an.feature_canvas.domain.use_cases.SelectElement
import com.an.feature_canvas.domain.use_cases.TransformElement
import com.an.feature_canvas.domain.use_cases.Undo
import com.an.feature_canvas.presentation.CanvasViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val canvasModule = module {
    factory { AddImage( get(), get(), get() ) }
    factory { DeleteElement( get() ) }
    factory { ReorderElements( get() ) }
    factory { SelectElement( get() ) }
    factory { TransformElement( get() ) }
    factory { Undo( get() ) }
    factory<PngFileSaver> { PngFileSaverImpl(get(), get()) }

    factory { CanvasUseCases(get(), get(), get(), get(), get(), get()) }

    viewModel { CanvasViewModel(get(), get(), get(), get(), get(), get()) }
}