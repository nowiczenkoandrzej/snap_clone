package com.an.core_project.data

import com.an.core_project.domain.Project
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectSummary

class ProjectRepositoryImpl(

): ProjectRepository {
    override fun load(id: Long): Project {
        TODO("Not yet implemented")
    }

    override fun saveCurrent() {
        TODO("Not yet implemented")
    }

    override fun loadThumbnails(): List<ProjectSummary> {
        TODO("Not yet implemented")
    }
}