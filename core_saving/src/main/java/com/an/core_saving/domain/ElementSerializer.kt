package com.an.core_saving.domain

import com.an.core_editor.domain.model.DomainElement

interface ElementSerializer {
    suspend fun saveElements(
        elements: List<DomainElement>,
        fileName: String? = null
    ): String
    suspend fun loadElements(fileName: String): List<DomainElement>
}