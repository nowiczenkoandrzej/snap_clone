package com.an.facefilters.core

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.an.facefilters.camera.presentation.CameraScreen
import com.an.facefilters.camera.presentation.CameraViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Camera.route
    ) {
        composable(route = Screen.Camera.route) {

            val viewModel = koinViewModel<CameraViewModel>()

            CameraScreen(viewModel)
        }

        composable(route = Screen.Canvas.route) {

        }

        composable(route = Screen.Galery.route) {

        }
    }

}