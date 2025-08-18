package com.an.feature_canvas.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.an.core_ui.ui.theme.fontSize
import com.an.core_ui.ui.theme.spacing
import com.an.feature_canvas.R
import com.an.feature_canvas.presentation.util.ToolType

@Composable
fun ToolsSelector(
    modifier: Modifier = Modifier,
    onToolSelected: (ToolType) -> Unit,
    onHidePanel: () -> Unit,
) {

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


            ToolsListItem(
                onClick = { onToolSelected(ToolType.PICK_IMAGE_FROM_GALLERY) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                imageVector = Icons.Default.Photo,
                name = stringResource(R.string.add_image)

            )

            ToolsListItem(
                onClick = { onToolSelected(ToolType.ADD_TEXT) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                imageVector = Icons.Default.TextFields,
                name = stringResource(R.string.add_text)
            )

            ToolsListItem(
                onClick = { onToolSelected(ToolType.ASPECT_RATIO) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                imageVector = Icons.Default.AspectRatio,
                name = stringResource(R.string.aspect_ratio)
            )


            ToolsListItem(
                onClick = { onToolSelected(ToolType.STICKERS) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                imageVector = Icons.Default.Star,
                name = stringResource(R.string.stickers)
            )
            ToolsListItem(
                onClick = { onToolSelected(ToolType.SAVE) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.small),
                imageVector = Icons.Default.Save,
                name = stringResource(R.string.save)
            )


        }
    }

}

