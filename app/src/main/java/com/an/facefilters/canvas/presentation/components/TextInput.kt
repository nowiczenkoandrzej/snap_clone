package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import com.an.facefilters.ui.theme.spacing

@Composable
fun TextInput(
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onShowColorPicker: () -> Unit,
    onSelectColor: (Color) -> Unit,
    onSelectFontFamily: (FontFamily) -> Unit,
    selectedColor: Color,
    selectedFont: FontFamily,
) {

    var text by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            .padding(MaterialTheme.spacing.extraSmall)
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        /*BasicTextField(
            value = text,
            onValueChange = {
                text = it
            },
            modifier = Modifier.focusRequester(focusRequester),
            textStyle = LocalTextStyle.current.copy(
                fontSize = 60.sp,
                color = selectedColor
            ),
        )

        Button(
            onClick = { onConfirm(text) }
        ) {
            Text(stringResource(R.string.add))
        }

        Spacer(Modifier.height(1.dp))*/

        FontSelector(
            selectedFont = selectedFont,
            onSelectFont = onSelectFontFamily
        )

        QuickColorPicker(
            selectedColor = selectedColor,
            onColorSelected = { color ->
                onSelectColor(color)
            },
            onOpenCustomColorPicker = onShowColorPicker,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.focusRequester(focusRequester),
            textStyle = TextStyle(
                fontFamily = selectedFont,
                color = Color.White,
            ),
            trailingIcon = {
                if(text.isNotBlank()) {
                    IconButton(onClick = {
                        onConfirm(text)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            }
        )
    }


}