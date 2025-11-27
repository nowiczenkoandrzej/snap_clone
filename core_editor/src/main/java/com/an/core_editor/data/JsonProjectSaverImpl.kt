package com.an.core_editor.data

import android.content.Context
import com.an.core_editor.data.model.DataElement
import com.an.core_editor.data.model.DataImageModel
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.presentation.mappers.toData
import com.an.core_editor.domain.JsonProjectSaver
import com.an.core_editor.presentation.mappers.toDomainElements
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class JsonProjectSaverImpl(
    private val context: Context
): JsonProjectSaver {
    override fun save(elements: List<DomainElement>): String {

        val fileName = "editor_state.json"

        val json = Json { prettyPrint = true }
        val serializedElements = elements.toData()

        val jsonString = json.encodeToString(elements)

        File(context.filesDir, fileName)
            .writeText(jsonString)

        return fileName
    }

    override fun load(fileName: String): List<DomainElement> {
        val jsonString = File(context.filesDir, "editor_state.json").readText()
        val json = Json { prettyPrint = true }

        val list = json.decodeFromString<List<DataElement>>(jsonString)

        return list.toDomainElements()

    }
}