package com.an.facefilters.canvas.presentation.components.panels

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.an.facefilters.R

@Composable
fun DrawingPanel(
    modifier: Modifier = Modifier,
    onShowColorPicker: () -> Unit,
    onChangeThickness: (Float) -> Unit
) {

    Row(
        modifier = modifier
    ) {
        Button(
            onClick = { onShowColorPicker() }
        ) {
            Text(stringResource(R.string.select_color))
        }
    }
    

}