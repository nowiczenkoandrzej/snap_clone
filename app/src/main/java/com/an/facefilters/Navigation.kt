package com.an.facefilters

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.an.feature_canvas.presentation.CanvasScreen
import com.an.feature_canvas.presentation.CanvasViewModel
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import com.an.feature_image_editing.presentation.screens.CroppingScreen
import com.an.feature_image_editing.presentation.screens.DrawingScreen
import com.an.feature_image_editing.presentation.screens.ImageEditingScreen
import com.an.feature_image_editing.presentation.screens.ImageFilterScreen
import com.an.feature_image_editing.presentation.screens.RubberScreen
import com.an.feature_stickers.presentation.CuttingScreen
import com.an.feature_stickers.presentation.StickerViewModel
import com.an.feature_stickers.presentation.StickersScreen
import com.an.feature_text.presentation.AddTextScreen
import com.an.feature_text.presentation.EditTextScreen
import com.an.feature_text.presentation.TextViewModel
import org.koin.androidx.compose.getViewModel

import org.koin.androidx.compose.koinViewModel

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
                    onNavigateToCuttingScreen = {
                        navController.navigate(Screen.Cutting.route)
                    },
                    popBackStack = {
                        navController.popBackStack()
                    }
                )

            }

            composable(route = Screen.Drawing.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.EditImage.route)
                }

                val viewModel: ImageEditingViewModel = getViewModel(viewModelStoreOwner = parentEntry)

                DrawingScreen(
                    viewModel = viewModel,
                    popBackStack = {
                        navController.popBackStack()
                    }
                )

            }

            composable(route = Screen.Filters.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.EditImage.route)
                }

                val viewModel: ImageEditingViewModel = getViewModel(viewModelStoreOwner = parentEntry)

                ImageFilterScreen(
                    viewModel = viewModel,
                    popBackStack = {
                        navController.popBackStack()
                    }
                )

            }

            composable(route = Screen.Crop.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.EditImage.route)
                }

                val viewModel: ImageEditingViewModel = getViewModel(viewModelStoreOwner = parentEntry)


                CroppingScreen(
                    viewModel = viewModel,
                    popBackStack = {
                        navController.popBackStack()
                    }
                )

            }

            composable(route = Screen.Rubber.route) { backStackEntry ->
                val parentEntry = remember(backStackEntry) {
                    navController.getBackStackEntry(Screen.EditImage.route)
                }

                val viewModel: ImageEditingViewModel = getViewModel(viewModelStoreOwner = parentEntry)

                RubberScreen(
                    viewModel = viewModel,
                    popBackStack = {
                        navController.popBackStack()
                    }
                )


            }


        }




        composable(route = Screen.Stickers.route) {
            val viewModel = koinViewModel<StickerViewModel>()

            StickersScreen(
                viewModel = viewModel,
                popBackStack = {
                    navController.popBackStack()
                }
            )

        }

        composable(route = Screen.Cutting.route) {
            val viewModel = koinViewModel<StickerViewModel>()
            CuttingScreen(
                viewModel = viewModel,
                popBackStack = {
                    navController.popBackStack()
                }
            )


        }

        composable(route = Screen.AddText.route) {

            val viewModel = koinViewModel<TextViewModel>()

            AddTextScreen(
                viewModel = viewModel,
                popBackStack = {
                    navController.popBackStack()
                }
            )


        }

        composable(route = Screen.EditText.route) {

            val viewModel = koinViewModel<TextViewModel>()

            EditTextScreen(
                viewModel = viewModel,
                popBackStack = {
                    navController.popBackStack()
                }
            )


        }







    }

}