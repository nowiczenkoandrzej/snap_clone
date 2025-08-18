package com.an.feature_text.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.an.core_ui.ui.theme.spacing
import com.an.feature_text.R
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    onColorSelected: (Color) -> Unit
) {
    val controller = rememberColorPickerController()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        HsvColorPicker(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            controller = controller,
            onColorChanged = { newColor ->
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
        AlphaSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.spacing.large),
            controller = controller
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))


        BrightnessSlider(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.spacing.large),
            controller = controller
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))


        AlphaTile(
            modifier = Modifier
                .size(MaterialTheme.spacing.extraLarge)
                .clip(RoundedCornerShape(MaterialTheme.spacing.small)),
            controller = controller
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))


        Button(
            onClick = {
                onColorSelected(controller.selectedColor.value)
            }
        ) {
            Text(stringResource(R.string.select_color))
        }



    }


}