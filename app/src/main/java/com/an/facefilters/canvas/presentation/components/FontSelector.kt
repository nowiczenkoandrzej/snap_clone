package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import com.an.facefilters.canvas.presentation.util.rememberFontList

@Composable
fun FontSelector(
    modifier: Modifier = Modifier,
    selectedFont: FontFamily,
    onSelectFont: (FontFamily) -> Unit
) {

    var showFontSelector by remember {
        mutableStateOf(false)
    }

    val fonts = rememberFontList()

    Row(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Box {
            TextButton(
                onClick = { showFontSelector = true }
            ) {
                Text(
                    text = "Aa",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = selectedFont
                )
            }
            DropdownMenu(
                expanded = showFontSelector,
                onDismissRequest = { showFontSelector = false },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                fonts.forEach {
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = it.name,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = it.fontFamily
                            )
                        },
                        onClick = {
                            onSelectFont(it.fontFamily)
                            showFontSelector = false
                        }
                    )
                }
            }
        }

    }

}