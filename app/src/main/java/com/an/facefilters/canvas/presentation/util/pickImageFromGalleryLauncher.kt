package com.an.facefilters.canvas.presentation.util

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.scale

@Composable
fun pickImageFromGalleryLauncher(
    onAction: (Bitmap) -> Unit
): ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?> {

    val context = LocalContext.current


    val launcher =  rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            val contentResolver = context.contentResolver
            val originalBitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))

            val displayMetrics = context.resources.displayMetrics

            val scale = minOf(
                displayMetrics.widthPixels.toFloat() / originalBitmap.width,
                displayMetrics.heightPixels.toFloat() / originalBitmap.height
            )

            val bitmap = originalBitmap.scale(
                (originalBitmap.width * scale).toInt(),
                (originalBitmap.height * scale).toInt()
            )

            onAction(bitmap)
        }
    }

    return launcher

}