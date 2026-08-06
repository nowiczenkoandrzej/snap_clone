package com.an.core_saving.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializedStickerModel(
    val rotationAngle: Float,
    val scale: Float,
    val position: SerializedPoint,
    val alpha: Float,
    val stickerPath: String,
): SerializedElement()