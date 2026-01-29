package com.an.core_project.domain

interface ProjectRepository {
    fun load(id: Int): Project
    fun saveCurrent()
    fun loadThumbnails(): List<ProjectSummary>

}
