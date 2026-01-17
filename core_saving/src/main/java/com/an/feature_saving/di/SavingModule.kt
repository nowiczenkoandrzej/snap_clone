package com.an.feature_saving.di

import com.an.feature_saving.data.BitmapSaverImpl
import com.an.feature_saving.data.JsonProjectSaverImpl
import com.an.feature_saving.data.PngFileSaverImpl
import com.an.feature_saving.domain.BitmapSaver
import com.an.feature_saving.domain.JsonProjectSaver
import com.an.feature_saving.domain.PngFileSaver
import org.koin.dsl.module

val savingModule = module {
    single<JsonProjectSaver> { JsonProjectSaverImpl(get()) }
    single<PngFileSaver> { PngFileSaverImpl(get(), get()) }
    single<BitmapSaver> { BitmapSaverImpl(get()) }

}