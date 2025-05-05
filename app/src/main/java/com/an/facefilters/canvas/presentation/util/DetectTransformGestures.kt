package com.an.facefilters.canvas.presentation.util

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateRotation
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputScope
import kotlin.math.abs

suspend fun PointerInputScope.detectTransformGesturesWithCallbacks(
    panZoomLock: Boolean = false,
    onGestureStart: () -> Unit = {},
    onGestureEnd: () -> Unit = {},
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit
) {
    forEachGesture {
        awaitPointerEventScope {
            val down = awaitFirstDown(requireUnconsumed = false)
            var pastTouchSlop = false
            var lockedToPanZoom = false

            onGestureStart()

            var zoom = 1f
            var rotation = 0f
            var pan = Offset.Zero

            val touchSlop = viewConfiguration.touchSlop
            val centroid = down.position

            do {
                val event = awaitPointerEvent()
                val changes = event.changes

                if (!pastTouchSlop) {
                    val zoomChange = event.calculateZoom()
                    val rotationChange = event.calculateRotation()
                    val panChange = event.calculatePan()

                    if (abs(zoomChange - 1) > 0.01f ||
                        abs(rotationChange) > 0.01f ||
                        panChange.getDistance() > touchSlop
                    ) {
                        pastTouchSlop = true
                    }
                }

                if (pastTouchSlop) {
                    val zoomChange = event.calculateZoom()
                    val rotationChange = event.calculateRotation()
                    val panChange = event.calculatePan()

                    zoom *= zoomChange
                    rotation += rotationChange
                    pan += panChange

                    onGesture(centroid, panChange, zoomChange, rotationChange)

                    changes.forEach { it.consume() }
                }
            } while (changes.any { it.pressed })

            onGestureEnd()
        }
    }
}