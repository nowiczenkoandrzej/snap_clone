package com.an.core_project.di

import com.an.core_project.data.ProjectRepositoryImpl
import com.an.core_project.data.ProjectSessionManagerImpl
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectSessionManager
import org.koin.dsl.module

val projectModule = module {

    single <ProjectRepository> { ProjectRepositoryImpl(get(), get()) }
    single <ProjectSessionManager> { ProjectSessionManagerImpl(get()) }


}