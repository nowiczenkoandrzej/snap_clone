package com.an.facefilters.canvas.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Draw
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
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
            type = ToolType.AddPhoto,
            name = stringResource(R.string.add_image),
            icon = Icons.Default.Draw
        ),
    )

    return remember { tools }

}