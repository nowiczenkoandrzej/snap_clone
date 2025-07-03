package com.an.facefilters.canvas.presentation.components.panels

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.canvas.presentation.components.ToolItem
import com.an.facefilters.canvas.presentation.util.rememberToolsList

@Composable
fun ImgPanel(
    onSelect: () -> Unit
) {

    val tools = rememberToolsList()
    val toolsMap = tools.associateBy { it.type }


    LazyRow {
        item {
            toolsMap[ToolType.CreateSticker]?.let {
                ToolItem(
                    tool = it
                )
            }
        }
    }
}