package com.an.core_project.domain

import com.an.core_editor.domain.model.DomainElement

data class Project(
    val id: Int? = null,
    val elements: List<DomainElement> = emptyList(),
    val aspectRatio: Float = 3/4f,
    val undos: List<List<DomainElement>> = emptyList(),
    val lastChange: Long = System.currentTimeMillis(),
    val thumbNail: String? = null,
    val selectedElementIndex: Int? = null
)
