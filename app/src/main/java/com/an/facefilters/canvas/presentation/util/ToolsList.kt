package com.an.facefilters.canvas.presentation.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.an.facefilters.R
import com.an.facefilters.canvas.domain.model.Tool
import com.an.facefilters.canvas.domain.model.ToolType

@Composable
fun rememberToolsList(): List<Tool> {

    val tools = listOf(
        Tool(
            type = ToolType.Pencil,
            name = stringResource(R.string.pencil),
            icon = Icons.Default.Draw
        ),
        Tool(
            type = ToolType.PickImageFromGallery,
            name = stringResource(R.string.add_image),
            icon = Icons.Default.Photo
        ),
        Tool(
            type = ToolType.RemoveBg,
            name = stringResource(R.string.remove_background),
            icon = Icons.Default.PhotoFilter
        ),
        Tool(
            type = ToolType.CreateSticker,
            name = stringResource(R.string.create_a_sticker),
            icon = Icons.Default.Star
        ),
        Tool(
            type = ToolType.CropImage,
            name = stringResource(R.string.crop_image),
            icon = Icons.Default.Crop
        ),
        Tool(
            type = ToolType.Text,
            name = stringResource(R.string.text),
            icon = Icons.Default.TextFields
        ),
        Tool(
            type = ToolType.AspectRatio,
            name = stringResource(R.string.aspect_ratio),
            icon = Icons.Default.AspectRatio
        ),
        Tool(
            type = ToolType.Stickers,
            name = stringResource(R.string.stickers),
            icon = Icons.Default.Stars
        ),
        Tool(
            type = ToolType.Filters,
            name = stringResource(R.string.filters),
            icon = Icons.Default.Filter
        ),
        Tool(
            type = ToolType.Save,
            name = stringResource(R.string.save),
            icon = Icons.Default.Save
        )



    )

    return remember { tools }

}