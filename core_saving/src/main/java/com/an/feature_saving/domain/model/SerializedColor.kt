package com.an.feature_saving.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializedColor(
    val red: Float,
    val green: Float,
    val blue: Float,
    val alpha: Float,
)

