package com.an.core_editor.data.model

import androidx.compose.ui.graphics.Color
import com.an.core_editor.domain.model.PathData
import com.an.core_editor.domain.model.Point
import kotlinx.serialization.Serializable

@Serializable
data class DataPathData(
    val color: DataColor,
    val path: List<DataPoint>,
    val thickness: Float
) {
    fun toDomain() = PathData(
        color = color.toDomain(),
        path = path.map { it.toDomain() },
        thickness = thickness
    )
}

fun PathData.toData() = DataPathData(
    path = path.map { it.toData() },
    thickness = thickness,
    color = color?.toData()
)