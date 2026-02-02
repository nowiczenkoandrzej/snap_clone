package com.an.core_saving.di

import com.an.core_saving.data.BitmapSaverImpl
import com.an.core_saving.data.ProjectDataSourceImpl
import com.an.core_saving.domain.BitmapSaver
import com.an.core_saving.domain.ProjectDataSource
import org.koin.dsl.module

var savingModule = module {
    single<BitmapSaver> { BitmapSaverImpl(get()) }
    single<ProjectDataSource> { ProjectDataSourceImpl(get()) }
}