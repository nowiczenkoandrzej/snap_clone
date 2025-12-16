package com.an.core_editor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializedRect(
    val left: Float,
    val bottom: Float,
    val right: Float,
    val top: Float,
)
