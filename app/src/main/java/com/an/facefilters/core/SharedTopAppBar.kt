package com.an.facefilters.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.an.facefilters.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SharedTopAppBar(
    navController: NavController,
) {

    val currentRoute by navController
        .currentBackStackEntryAsState()




    TopAppBar(
        title = {
            Text(
                text = currentRoute
                    ?.destination
                    ?.route ?: stringResource(R.string.app_name))
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(Screen.Settings.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null
                )
            }
        }
    )
}