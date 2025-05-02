package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.an.facefilters.R
import com.an.facefilters.canvas.domain.model.Tool
import com.an.facefilters.canvas.domain.model.ToolType

@Composable
fun ToolsSelector(
    modifier: Modifier = Modifier,
    onToolSelected: (ToolType) -> Unit,
    onHidePanel: () -> Unit
) {
    val tools = rememberToolsList()


    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.tools))
            IconButton(
                onClick = { onHidePanel() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(tools) { tool ->
                ToolItem(
                    tool = tool,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable {
                            onToolSelected(tool.type)
                        }
                )
            }
        }
    }


}