package com.an.core_editor.data.model

import com.an.core_editor.domain.model.Point
import kotlinx.serialization.Serializable

@Serializable
data class DataPoint(
    val x: Float,
    val y: Float
) {
    fun toDomain() = Point(x, y)
}

fun List<DataPoint>.toDomain(): List<Point> {
    return this.map {
        it.toDomain()
    }
}

fun List<Point>.toData(): List<DataPoint> {
    return this.map {
        DataPoint(it.x, it.y)
    }
}
