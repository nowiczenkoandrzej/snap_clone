package com.an.core_project.di

import com.an.core_project.data.EntityMapper
import com.an.core_project.data.ProjectEditorImpl
import com.an.core_project.data.ProjectRepositoryImpl
import com.an.core_project.domain.ProjectEditor
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.presentation.HomeViewmodel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val projectModule = module {

    single <ProjectRepository> { ProjectRepositoryImpl(get(), get(), get(), get()) }
    single <ProjectEditor> { ProjectEditorImpl(get(), get()) }
    single { EntityMapper(get(), get()) }

    viewModel { HomeViewmodel(get()) }


}