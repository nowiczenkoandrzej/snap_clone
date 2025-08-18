package com.an.feature_text.di

import com.an.feature_text.domain.AddText
import com.an.feature_text.domain.TextUseCases
import com.an.feature_text.domain.UpdateFontFamily
import com.an.feature_text.domain.UpdateTextColor
import com.an.feature_text.presentation.TextViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val textModule = module {
    factory { AddText( get() ) }
    factory { UpdateFontFamily( get() ) }
    factory { UpdateTextColor( get() ) }

    factory { TextUseCases( get(), get(), get() ) }

    viewModel { TextViewModel(get(), get()) }
}