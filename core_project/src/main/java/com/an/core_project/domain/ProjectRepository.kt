package com.an.core_project.domain

import com.an.core_editor.domain.model.Result
import kotlinx.coroutines.flow.StateFlow

interface ProjectRepository {

    val session: StateFlow<Project?>

    suspend fun loadProject(id: Long)
    suspend fun saveProject()
    suspend fun loadProjectSummaries(): List<ProjectSummary>
    suspend fun startNewProject()
    suspend fun updateProject(
        saveUndo: Boolean = true,
        transform: (Project) -> Result<Project>
    ): Result<Unit>

}


