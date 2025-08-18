package com.an.feature_image_editing.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixNormal
import androidx.compose.material.icons.filled.Crop
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Filter
import androidx.compose.material.icons.filled.PhotoFilter
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.an.core_ui.ui.theme.spacing
import com.an.feature_image_editing.R

@Composable
fun ImageActionPanel(
    onNavigateToDrawingScreen: () -> Unit,
    onNavigateToFilterScreen: () -> Unit,
    onNavigateToCroppingScreen: () -> Unit,
    onNavigateToRubberScreen: () -> Unit,
    onNavigateToCreateStickerScreen: () -> Unit,
    onRemoveBackground: () -> Unit,
    onDelete: () -> Unit

) {
    LazyRow (
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        contentPadding = PaddingValues(MaterialTheme.spacing.medium),
    ) {
        item {
            ToolItem(
                imageVector = Icons.Default.Draw,
                name = stringResource(R.string.pencil),
                onCLick = { onNavigateToDrawingScreen() }
            )
        }
        item {
            ToolItem(
                imageVector = Icons.Default.Crop,
                name = stringResource(R.string.crop_image),
                onCLick = { onNavigateToCroppingScreen() }
            )
        }
        item {
            ToolItem(
                imageVector = Icons.Default.Filter,
                name = stringResource(R.string.filters),
                onCLick = { onNavigateToFilterScreen() }
            )
        }
        item {
            ToolItem(
                imageVector = Icons.Default.PhotoFilter,
                name = stringResource(R.string.remove_background),
                onCLick = { onRemoveBackground() }
            )
        }
        item {
            ToolItem(
                imageVector = Icons.Default.AutoFixNormal,
                name = stringResource(R.string.rubber),
                onCLick = { onNavigateToRubberScreen() }
            )
        }
        item {
            ToolItem(
                imageVector = Icons.Default.Star,
                name = stringResource(R.string.convert_to_sticker),
                onCLick = { onNavigateToCreateStickerScreen() }
            )
        }
        item {
            ToolItem(
                imageVector = Icons.Default.Delete,
                name = stringResource(R.string.delete),
                onCLick = { onDelete() }
            )
        }
    }
}