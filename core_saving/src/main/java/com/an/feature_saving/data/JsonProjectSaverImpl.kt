package com.an.feature_saving.data

import android.content.Context
import com.an.core_editor.domain.model.DomainElement
import com.an.feature_saving.domain.JsonProjectSaver

class JsonProjectSaverImpl(
    private val context: Context
): JsonProjectSaver {
    override fun save(elements: List<DomainElement>): String {

        val fileName = "editor_state.json"

        /*val json = Json {
            prettyPrint = true
            serializersModule = SerializersModule {
                polymorphic(SerializedElement::class) {
                    subclass(SerializedTextModel::class)
                    subclass(SerializedImageModel::class)
                    subclass(SerializedStickerModel::class)
                }
            }
        }
        val serializedElements = elements.toData()

        val jsonString = json.encodeToString(serializedElements)

        File(context.filesDir, fileName)
            .writeText(jsonString)
*/
        return fileName
    }

    override fun load(fileName: String): List<DomainElement> {
        /*val jsonString = File(context.filesDir, fileName).readText()
        val json = Json { prettyPrint = true }

        val list = json.decodeFromString<List<SerializedElement>>(jsonString)

        Log.d("TAG", "load: $list")*/

        //return list.toDomainElements()

        TODO()

    }
}