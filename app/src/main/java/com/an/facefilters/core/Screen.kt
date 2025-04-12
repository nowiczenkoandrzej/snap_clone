package com.an.facefilters.core

sealed class Screen(val route: String) {
    object Camera: Screen(route = "camera")
    object Canvas: Screen(route = "canvas")
    object Galery: Screen(route = "galery")
}