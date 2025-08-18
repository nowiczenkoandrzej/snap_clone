package com.an.feature_canvas.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.core_editor.presentation.UiElement
import com.an.core_editor.presentation.UiImageModel
import com.an.core_editor.presentation.UiTextModel

@Composable
fun ElementThumbNail(
    modifier: Modifier = Modifier,
    element: UiElement,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val borderColor = if(isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }

    Box(
        modifier = modifier
            .clickable { onClick() }
            .border(4.dp, borderColor, RoundedCornerShape(8.dp))
            .background(Color.Transparent)
    ) {
        when(element){
            is UiImageModel -> {
                Image(
                    bitmap = element.bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxSize()
                )
            }
            is UiTextModel -> {
                Text(
                    text = element.text,
                    fontSize = 20.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(8.dp),
                    color = element.fontColor,
                    fontFamily = element.fontItem.fontFamily
                )

            }
            else -> {}
        }
    }


}