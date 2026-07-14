package com.an.core_project.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectThumbnail
import com.an.core_project.domain.toThumbnail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewmodel(
    private val projectRepository: ProjectRepository
): ViewModel() {


    private val _savedProjects = MutableStateFlow<List<ProjectThumbnail>>(emptyList())
    val savedProjects = _savedProjects.asStateFlow()


    init {
        viewModelScope.launch {
            _savedProjects.value = projectRepository
                .loadProjectSummaries()
                .map { summary ->
                    summary.toThumbnail()
                }

        }
    }

    fun loadProject(id: Long) {
        viewModelScope.launch {
           // sessionManager.startSession(id)
        }
    }

    fun startNewProject() {
        viewModelScope.launch {

        }
    }






}