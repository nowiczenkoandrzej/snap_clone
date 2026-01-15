package com.an.feature_drawing.di

import com.an.feature_drawing.domain.use_cases.CutImage
import com.an.feature_drawing.domain.use_cases.DrawingUseCases
import com.an.feature_drawing.domain.use_cases.ErasePathFromBitmap
import com.an.feature_drawing.domain.use_cases.SaveDrawings
import com.an.feature_drawing.presentation.DrawingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val drawingModule = module {
    viewModel { DrawingViewModel(get(), get(), get(), get()) }

    factory { CutImage(get()) }
    factory { ErasePathFromBitmap() }
    factory { SaveDrawings(get()) }

    factory { DrawingUseCases(get(), get(), get()) }


}