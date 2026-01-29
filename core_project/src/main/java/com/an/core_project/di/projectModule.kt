package com.an.core_project.di

import com.an.core_project.data.ProjectSessionManagerImpl
import com.an.core_project.domain.ProjectSessionManager
import org.koin.dsl.module

val projectModule = module {

    single <ProjectSessionManager> { ProjectSessionManagerImpl(get()) }


}