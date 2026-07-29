package com.an.core_project.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_project.domain.ProjectRepository
import com.an.core_project.domain.ProjectThumbnail
import com.an.core_project.domain.toThumbnail
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewmodel(
    private val projectRepository: ProjectRepository
): ViewModel() {


    private val _savedProjects = MutableStateFlow<List<ProjectThumbnail>>(emptyList())
    val savedProjects = _savedProjects.asStateFlow()

    private val _events = Channel<HomeScreenEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

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
            projectRepository.loadProject(id)
            _events.send(HomeScreenEvent.LoadProject)
        }
    }

    fun startNewProject() {
        viewModelScope.launch {
            projectRepository.startNewProject()
            _events.send(HomeScreenEvent.StartNewProject)
        }
    }






}