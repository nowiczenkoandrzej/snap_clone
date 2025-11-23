package com.an.feature_canvas.presentation.components

import android.graphics.BitmapFactory
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.core_editor.presentation.model.UiElement
import com.an.core_editor.presentation.model.UiImageModel
import com.an.core_editor.presentation.model.UiStickerModel
import com.an.core_editor.presentation.model.UiTextModel

@Composable
fun ElementThumbNail(
    modifier: Modifier = Modifier,
    element: UiElement,
    isSelected: Boolean,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {

    val context = LocalContext.current

    val borderColor = if(isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color.Transparent
    }
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        label = "thumbnailScale"
    )

    Box(
        modifier = modifier
            .clickable { onClick() }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .border(4.dp, borderColor, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
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

                AutoSizeText(
                    text = element.text,
                    color = element.fontColor,
                    fontFamily = element.fontItem.fontFamily,
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Center),
                    maxFontSize = 40.sp,
                    minFontSize = 10.sp,
                )

            }
            is UiStickerModel -> {
                val inputStream = context.assets.open(element.stickerPath)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .fillMaxSize()
                )
            }
            else -> {}


        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }

                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }


}