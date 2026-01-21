package com.an.core_saving.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializedRect(
    val left: Float,
    val bottom: Float,
    val right: Float,
    val top: Float,
)