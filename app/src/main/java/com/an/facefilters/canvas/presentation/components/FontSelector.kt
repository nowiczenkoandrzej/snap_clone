package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.an.facefilters.canvas.presentation.util.rememberFontList

@Composable
fun FontSelector(
    modifier: Modifier = Modifier,
    selectedFont: FontFamily,
    onFontSelected: (FontFamily) -> Unit
) {

    val listState = rememberLazyListState()

    val fonts = rememberFontList()


    LaunchedEffect(selectedFont) {
        val index = fonts.indexOfFirst { it.fontFamily == selectedFont }
        if (index >= 0) {
            listState.animateScrollToItem(index)
        }
    }

    LazyRow(
        modifier = modifier,
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        items(fonts) { fontItem ->
            FilterChip(
                selected = selectedFont == fontItem.fontFamily,
                onClick = { onFontSelected(fontItem.fontFamily) },
                label = {
                    Text(
                        text = fontItem.name,
                        fontFamily = fontItem.fontFamily
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }


}