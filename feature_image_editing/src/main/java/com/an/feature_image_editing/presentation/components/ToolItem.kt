package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.an.facefilters.canvas.domain.model.Tool
import com.an.facefilters.canvas.domain.model.ToolType
import com.an.facefilters.ui.theme.fontSize
import com.an.facefilters.ui.theme.spacing

@Composable
fun ToolItem(
    modifier: Modifier = Modifier,
    tool: Tool,
) {
    var isOverflowing by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(MaterialTheme.spacing.small),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            imageVector = tool.icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.size(32.dp)

        )
        Spacer(Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = tool.name,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.fontSize.medium,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Clip,
            onTextLayout = { result ->
                isOverflowing = result.didOverflowWidth
            },
            modifier = Modifier
                .width(72.dp)
                .basicMarquee(),
            textAlign = if(isOverflowing) TextAlign.Start else TextAlign.Center
        )
    }

}