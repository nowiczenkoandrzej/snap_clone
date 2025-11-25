package com.an.core_editor.data.model

import androidx.compose.ui.geometry.Rect
import com.an.core_editor.domain.model.Point
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@SerialName("image")
data class DataImageModel(
    val id: String,
    val imagePath: String,
    val rotationAngle: Float = 0f,
    val scale: Float = 1f,
    val position: DataPoint = DataPoint(0f, 0f),
    val alpha: Float = 1f,
    val edits: List<DataImageEdit> = emptyList(),
    val currentFilter: String,
    val version: Long
): DataElement