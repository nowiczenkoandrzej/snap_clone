package com.an.facefilters.core

sealed class Screen(val route: String) {
    object Camera: Screen(route = "Camera")
    object Canvas: Screen(route = "Canvas")
    object Gallery: Screen(route = "gallery")
    object Home: Screen(route = "Home")
    object Settings: Screen(route = "Settings")
}