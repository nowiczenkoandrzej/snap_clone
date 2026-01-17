package com.an.feature_saving.domain

import com.an.core_editor.domain.model.DomainElement

interface JsonProjectSaver {
    fun save(elements: List<DomainElement>): String

    fun load(fileName: String): List<DomainElement>
}