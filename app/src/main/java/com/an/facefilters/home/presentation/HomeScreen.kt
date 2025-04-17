package com.an.facefilters.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.an.facefilters.R
import com.an.facefilters.core.Screen
import com.an.facefilters.ui.theme.spacing

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {

        Button(
            onClick = {
                navController.navigate(Screen.Camera.route)
            }
        ) {
            Text(stringResource(R.string.take_a_photo))
        }

        Button(
            onClick = {
                navController.navigate(Screen.Canvas.route)
            }
        ) {
            Text(stringResource(R.string.make_project))
        }

    }
}