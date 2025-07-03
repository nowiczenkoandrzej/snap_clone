package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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