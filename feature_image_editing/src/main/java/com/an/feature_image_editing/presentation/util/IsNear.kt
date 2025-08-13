package com.an.facefilters.canvas.presentation.util

import androidx.compose.ui.geometry.Offset
import java.lang.Math.pow
import kotlin.math.sqrt

fun Offset.isNear(p1: Offset): Boolean {
    val distance = sqrt(pow((this.x - p1.x).toDouble(), 2.0) + pow((this.y - p1.y).toDouble(), 2.0))

    return distance < 100.0
}