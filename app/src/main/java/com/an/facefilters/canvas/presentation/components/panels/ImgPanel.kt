package com.an.facefilters.canvas.presentation.components.panels

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.presentation.components.ToolItem
import com.an.facefilters.canvas.presentation.util.rememberToolsList
import com.an.facefilters.ui.theme.spacing

@Composable
fun ImgPanel(
    onToolSelected: (ToolType) -> Unit,
) {

    val tools = rememberToolsList()
    val toolsMap = tools.associateBy { it.type }


    LazyRow (
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
    ){
        item {
            toolsMap[ToolType.Pencil]?.let {
                ToolItem(
                    tool = it,
                    modifier = Modifier
                        .clickable { onToolSelected(it.type) }
                )
            }
        }

        item {
            toolsMap[ToolType.CropImage]?.let {
                ToolItem(
                    tool = it,
                    modifier = Modifier
                        .clickable { onToolSelected(it.type) }
                        .padding(4.dp)
                )
            }
        }
        item {
            toolsMap[ToolType.Filters]?.let {
                ToolItem(
                    tool = it,
                    modifier = Modifier
                        .clickable { onToolSelected(it.type) }
                )
            }
        }
        item {
            toolsMap[ToolType.RemoveBg]?.let {
                ToolItem(
                    tool = it,
                    modifier = Modifier
                        .clickable { onToolSelected(it.type) }
                )
            }
        }
        item {
            toolsMap[ToolType.Rubber]?.let {
                ToolItem(
                    tool = it,
                    modifier = Modifier
                        .clickable { onToolSelected(it.type) }
                )
            }
        }

        item {
            toolsMap[ToolType.CreateSticker]?.let {
                ToolItem(
                    tool = it,
                    modifier = Modifier
                        .clickable { onToolSelected(it.type) }
                )
            }
        }

        item {
            toolsMap[ToolType.Delete]?.let {
                ToolItem(
                    tool = it,
                    modifier = Modifier
                        .clickable { onToolSelected(it.type) }
                )
            }
        }



    }
}