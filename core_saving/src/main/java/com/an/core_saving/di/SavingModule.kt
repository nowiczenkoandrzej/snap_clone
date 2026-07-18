package com.an.core_saving.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.an.core_saving.ProjectDatabase
import com.an.core_saving.data.BitmapSaverImpl
import com.an.core_saving.data.ElementSerializerImpl
import com.an.core_saving.data.ProjectDataSourceImpl
import com.an.core_saving.domain.BitmapSaver
import com.an.core_saving.domain.ElementSerializer
import com.an.core_saving.domain.ProjectDataSource
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

var savingModule = module {
    single<BitmapSaver> { BitmapSaverImpl(get()) }
    single<ProjectDataSource> { ProjectDataSourceImpl(get()) }
    single<ElementSerializer> { ElementSerializerImpl() }

    single<SqlDriver> {
        AndroidSqliteDriver(
            schema = ProjectDatabase.Schema,
            context = androidContext(),
            name = "ProjectDatabase"
        )
    }
    single { ProjectDatabase(get()) }
}