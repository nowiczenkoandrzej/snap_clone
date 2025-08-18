package com.an.core_editor.domain.model

data class Point(
    val x: Float,
    val y: Float
) {
    companion object {
        val ZERO = Point(0f, 0f)
    }
    fun plus(delta: Point): Point {
        return Point(
            x = this.x + delta.x,
            y = this.y + delta.y
        )
    }
}
