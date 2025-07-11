package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.an.facefilters.R
import com.an.facefilters.canvas.domain.model.Tool
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.presentation.util.rememberToolsList
import com.an.facefilters.ui.theme.fontSize
import com.an.facefilters.ui.theme.spacing

@Composable
fun ToolsSelector(
    modifier: Modifier = Modifier,
    onToolSelected: (ToolType) -> Unit,
    onHidePanel: () -> Unit
) {
    val tools = rememberToolsList()
    val toolsMap = tools.associateBy { it.type }

    Column(
        modifier = modifier
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.tools),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = MaterialTheme.fontSize.large
            )
            IconButton(
                onClick = { onHidePanel() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {


            toolsMap[ToolType.PickImageFromGallery]?.let {
                ToolsListItem(
                    tool = it,
                    onClick = { onToolSelected(it.type) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.small)
                )
            }
            toolsMap[ToolType.Text]?.let {
                ToolsListItem(
                    tool = it,
                    onClick = { onToolSelected(it.type) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.small)
                )
            }

            toolsMap[ToolType.AspectRatio]?.let {
                ToolsListItem(
                    tool = it,
                    onClick = { onToolSelected(it.type) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.small)
                )
            }

            toolsMap[ToolType.Stickers]?.let {
                ToolsListItem(
                    tool = it,
                    onClick = { onToolSelected(it.type) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.spacing.small)
                )
            }


        }
    }

}

@Composable
fun ToolsListItem(
    modifier: Modifier = Modifier,
    tool: Tool,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = tool.icon,
            contentDescription = null,
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
        )
        Text(tool.name)
    }


}