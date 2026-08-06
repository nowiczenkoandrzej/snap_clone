package com.an.core_saving.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class SerializedElement {

    @Serializable
    @SerialName("image")
    data class SerializedImageModel(
        val id: String,
        val imagePath: String,
        val rotationAngle: Float = 0f,
        val scale: Float = 1f,
        val position: SerializedPoint = SerializedPoint(0f, 0f),
        val alpha: Float = 1f,
        val edits: List<SerializedImageEdit> = emptyList(),
        val currentFilter: String,
        val version: Long
    ): SerializedElement()

    @Serializable
    @SerialName("sticker")
    data class SerializedStickerModel(
        val rotationAngle: Float,
        val scale: Float,
        val position: SerializedPoint,
        val alpha: Float,
        val stickerPath: String,
    ): SerializedElement()

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
    ): SerializedElement()
}