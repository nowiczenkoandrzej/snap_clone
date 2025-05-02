package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.an.facefilters.canvas.domain.model.Tool
import com.an.facefilters.canvas.domain.model.ToolType

@Composable
fun ToolItem(
    modifier: Modifier = Modifier,
    tool: Tool,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        Icon(
            imageVector = tool.icon,
            contentDescription = null
        )
        Text(tool.name)
    }

}