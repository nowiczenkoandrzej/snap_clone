package com.an.facefilters.home.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.an.facefilters.R
import com.an.facefilters.core.Screen
import com.an.facefilters.ui.theme.spacing
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {

    val cameraPermissionsState = rememberPermissionState(Manifest.permission.CAMERA)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {


        Button(
            onClick = {
                when {
                    cameraPermissionsState.status.isGranted -> {
                        navController.navigate(Screen.Canvas.route)
                    }
                    cameraPermissionsState.status.shouldShowRationale -> {
                        cameraPermissionsState.launchPermissionRequest()
                    }
                    else -> {
                        Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.fromParts("package", context.packageName, null)
                        ).also { intent ->
                            context.startActivity(intent)
                        }
                    }

                }
            }
        ) {
            Text(stringResource(R.string.take_a_photo))
        }

        Button(
            onClick = {

            }
        ) {
            Text(stringResource(R.string.make_project))
        }

    }
}