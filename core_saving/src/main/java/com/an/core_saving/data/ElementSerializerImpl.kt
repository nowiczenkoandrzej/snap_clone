package com.an.core_saving.data

import android.content.Context
import com.an.core_editor.domain.model.DomainElement
import com.an.core_saving.data.mappers.toDomainElements
import com.an.core_saving.data.mappers.toSerializedElements
import com.an.core_saving.domain.ElementSerializer
import com.an.core_saving.domain.model.SerializedElement
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import java.util.UUID

class ElementSerializerImpl(
    private val context: Context
): ElementSerializer {

    private val json = Json {
        classDiscriminator = "type"
        encodeDefaults = true
        ignoreUnknownKeys = true
    }

    override suspend fun saveElements(elements: List<DomainElement>, fileName: String?): String {


        val serializedElements = elements.toSerializedElements()

        val jsonString = json.encodeToString(
            ListSerializer(SerializedElement.serializer()),
            serializedElements
        )

        val savedFileName = fileName ?: "${UUID.randomUUID()}.json"

        val file = File(context.filesDir, savedFileName)
        file.writeText(jsonString)

        return savedFileName

    }


    override suspend fun loadElements(fileName: String): List<DomainElement> {
        val loadedJson = File(context.filesDir, fileName).readText()

        if(loadedJson.isEmpty()) return emptyList()

        return json.decodeFromString(
            ListSerializer(SerializedElement.serializer()),
            loadedJson
        ).toDomainElements()
    }
}