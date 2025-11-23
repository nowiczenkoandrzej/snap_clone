package com.an.core_editor.data.model

import com.an.core_editor.domain.DomainColor
import kotlinx.serialization.Serializable

@Serializable
data class DataColor(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float,
)

