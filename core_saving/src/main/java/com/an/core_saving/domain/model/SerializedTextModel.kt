package com.an.core_saving.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("text")
data class SerializedTextModel(
    val rotationAngle: Float,
    val scale: Float,
    val position: SerializedPoint,
    val alpha: Float,
    val text: String,
    val fontSize: Float,
    val fontColor: SerializedColor,
    val fontFamily: String
): SerializedElement