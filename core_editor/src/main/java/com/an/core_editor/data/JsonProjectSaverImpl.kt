package com.an.core_editor.data

import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.presentation.mappers.toData
import com.an.core_editor.domain.JsonProjectSaver
import kotlinx.serialization.json.Json

class JsonProjectSaverImpl: JsonProjectSaver {
    override fun save(elements: List<DomainElement>): String {

        val fileName = "editor_state.json"

        val json = Json { prettyPrint = true }
        val serializedElements = elements.toData()

       // val jsonString =

        TODO("Not yet implemented")

    }

    override fun load(fileName: String): List<DomainElement> {
        TODO("Not yet implemented")
    }
}