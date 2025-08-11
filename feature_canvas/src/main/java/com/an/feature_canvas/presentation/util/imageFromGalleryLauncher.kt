package com.an.feature_canvas.presentation

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp


@Composable
fun pickImageFromGalleryLauncher(
    density: Density,
    onAction: (String, Int, Int, Float) -> Unit,
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {

    val context = LocalContext.current

    val launcher =  rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.path?.let { path ->

            val displayMetrics = context.resources.displayMetrics

            val padding = with(density) { 8.dp.toPx() * 2 }

            onAction(path, displayMetrics.widthPixels, displayMetrics.heightPixels, padding)

        }
    }

    return launcher

}