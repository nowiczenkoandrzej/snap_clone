package com.an.facefilters.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.an.facefilters.camera.presentation.CameraScreen
import com.an.facefilters.camera.presentation.CameraViewModel
import com.an.facefilters.canvas.presentation.CanvasScreen
import com.an.facefilters.canvas.presentation.CanvasViewModel
import com.an.facefilters.home.presentation.HomeScreen
import com.an.facefilters.home.presentation.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        composable(route = Screen.Home.route) {
            val viewModel = koinViewModel<HomeViewModel>()
            HomeScreen(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(route = Screen.Settings.route) {
            Text("Settings")
        }


        composable(route = Screen.Camera.route) {
            val viewModel = koinViewModel<CameraViewModel>()
            CameraScreen(viewModel)
        }

        composable(route = Screen.Canvas.route) {
            val viewModel = koinViewModel<CanvasViewModel>()

            CanvasScreen(
                viewModel = viewModel,
                navController = navController
            )
        }


    }

}