package com.an.core_editor.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DataColor(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float,
)

