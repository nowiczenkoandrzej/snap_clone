package com.an.feature_canvas.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.an.core_ui.ui.theme.spacing

@Composable
fun ToolsListItem(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    name: String,
    onClick: () -> Unit
) {

    Row(
        modifier = modifier
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = null,
            modifier = Modifier
                .padding(MaterialTheme.spacing.small)
        )
        Text(name)
    }


}