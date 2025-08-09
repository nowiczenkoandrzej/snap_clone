package com.an.facefilters.canvas.presentation.components.panels

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.facefilters.canvas.presentation.components.AspectRatioItem
import com.an.facefilters.canvas.presentation.util.AspectRatio

@Composable
fun AspectRatioPanel(
    modifier: Modifier = Modifier,
    onAspectRatioSelected: (Float) -> Unit,
    selectedRatio: Float
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 12.dp)
            .height(60.dp / AspectRatio.RATIO_9_21),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(AspectRatio.ALL_RATIOS) { (label, ratio) ->
            AspectRatioItem(
                label = label,
                ratio = ratio,
                isSelected = ratio == selectedRatio
            ) {
                onAspectRatioSelected(ratio)
            }
        }
    }
}