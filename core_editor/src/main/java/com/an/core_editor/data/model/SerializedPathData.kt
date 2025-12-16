package com.an.core_editor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializedPathData(
    val color: SerializedColor,
    val path: List<SerializedPoint>,
    val thickness: Float
)


