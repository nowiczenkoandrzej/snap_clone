package com.an.core_editor.data.model

import com.an.core_editor.domain.model.Point
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("sticker")
data class DataStickerModel(
    val rotationAngle: Float,
    val scale: Float,
    val position: DataPoint,
    val alpha: Float,
    val stickerPath: String,
): DataElement