package com.an.core_editor.data.model

import com.an.core_editor.domain.model.Point
import kotlinx.serialization.Serializable

@Serializable
data class DataStickerModel(
    val rotationAngle: Float,
    val scale: Float,
    val position: DataPoint,
    val alpha: Float,
    val stickerPath: String,
)