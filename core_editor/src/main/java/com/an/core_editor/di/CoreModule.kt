package com.an.core_editor.di

import com.an.core_editor.data.BitmapCache
import com.an.core_editor.data.EditorRepositoryImpl
import com.an.core_editor.domain.EditorRepository
import org.koin.dsl.module

val coreModule = module {
    single<EditorRepository> { EditorRepositoryImpl() }
    single { BitmapCache() }
}