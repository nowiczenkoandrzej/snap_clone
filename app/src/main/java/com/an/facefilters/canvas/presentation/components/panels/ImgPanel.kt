package com.an.facefilters.canvas.presentation.components.panels

import android.graphics.Bitmap
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.data.filters.getFiltersList

@Composable
fun ImgPanel(
    onFilterSelected: (PhotoFilter) -> Unit,
) {


    var currentFilter by remember { mutableStateOf("Original") }

    LazyRow(
        contentPadding = PaddingValues(16.dp),
    ) {

        items(getFiltersList()) { filter ->
            FilterChip(
                selected = currentFilter == filter.name,
                onClick =  {
                    currentFilter = filter.name
                    onFilterSelected(filter)
                },
                label = {
                    Text(filter.name)
                }
            )
        }
    }
}