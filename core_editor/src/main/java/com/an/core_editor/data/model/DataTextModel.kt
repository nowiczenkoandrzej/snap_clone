package com.an.core_editor.data.model

import com.an.core_editor.domain.DomainColor
import com.an.core_editor.domain.DomainFontFamily
import com.an.core_editor.domain.model.Point
import kotlinx.serialization.Serializable

@Serializable
data class DataTextModel(
    val rotationAngle: Float,
    val scale: Float,
    val position: DataPoint,
    val text: String,
    val fontSize: Float,
    val fontColor: DataColor,
    val fontFamily: String
)