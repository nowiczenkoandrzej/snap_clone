package com.an.core_saving.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializedPathData(
    val color: SerializedColor,
    val path: List<SerializedPoint>,
    val thickness: Float
)