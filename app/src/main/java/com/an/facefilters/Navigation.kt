package com.an.facefilters

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.an.feature_canvas.presentation.CanvasScreen
import com.an.feature_canvas.presentation.CanvasViewModel
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.screens.CroppingScreen
import com.an.feature_image_editing.presentation.screens.ImageEditingScreen
import com.an.feature_image_editing.presentation.screens.ImageFilterScreen
import org.koin.androidx.compose.getViewModel

import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.compose.navigation.koinNavViewModel

@Composable
fun Navigation(
    navController: NavHostController,
    modifier: Modifier
) {



    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Canvas.route
    ) {

        composable(route = Screen.Home.route) {

        }

        composable(route = Screen.Settings.route) {
            Text("Settings")
        }

        composable(route = Screen.Canvas.route) {
            val canvasViewModel = koinViewModel<CanvasViewModel>()

            CanvasScreen(
                viewModel = canvasViewModel,
                navigateToAddTextScreen = {
                    navController.navigate(Screen.AddText.route)
                },
                navigateToStickerScreen = {
                    navController.navigate(Screen.Stickers.route)
                },
                navigateToEditTextScreen = {
                    navController.navigate(Screen.EditText.route)
                },
                navigateToEditImageScreen = {
                    navController.navigate(Screen.EditImage.route)
                },
            )

        }



        navigation(
            route = Screen.EditImage.route,
            startDestination = "image_editing_default"
        ) {


            composable(route = "image_editing_default") {
                val editingViewModel = koinViewModel<ImageEditingViewModel>()
                ImageEditingScreen(
                    viewModel = editingViewModel,
                    onNavigateToDrawingScreen = {
                        navController.navigate(Screen.Drawing.route)
                    },
                    onNavigateToFilterScreen = {
                        navController.navigate(Screen.Filters.route)
                    },
                    onNavigateToCroppingScreen = {
                        navController.navigate(Screen.Crop.route)
                    },
                    onNavigateToRubberScreen = {
                        navController.navigate(Screen.Rubber.route)
                    },
                    onNavigateToCreateStickerScreen = {
                        navController.navigate(Screen.CreateSticker.route)
                    }
                )

            }

            composable(route = Screen.Drawing.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.EditImage.route)
                }

                val viewModel: ImageEditingViewModel = getViewModel(viewModelStoreOwner = parentEntry)

                /*DrawingScreen(
                    viewModel = viewModel
                )*/

            }

            composable(route = Screen.Filters.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.EditImage.route)
                }

                val viewModel: ImageEditingViewModel = getViewModel(viewModelStoreOwner = parentEntry)

                ImageFilterScreen(
                    viewModel = viewModel
                )

            }

            composable(route = Screen.Crop.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.EditImage.route)
                }

                val viewModel: ImageEditingViewModel = getViewModel(viewModelStoreOwner = parentEntry)


                CroppingScreen(
                    viewModel = viewModel
                )

            }

            composable(route = Screen.Rubber.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.EditImage.route)
                }

                val viewModel: ImageEditingViewModel = getViewModel(viewModelStoreOwner = parentEntry)



            }


        }




        composable(route = Screen.Stickers.route) {


        }

        composable(route = Screen.CreateSticker.route) {


        }







    }

}