package com.an.facefilters.canvas.domain.model

sealed interface Tools {
    object AddPhoto: Tools


}

data class Tool(
    val type: Tools,
    val name: String
)
