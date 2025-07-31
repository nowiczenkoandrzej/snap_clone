package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
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
    isColorPickerVisible: Boolean
) {

    var text by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(isColorPickerVisible){
        if(!isColorPickerVisible) {
            keyboardController?.show()
        } else {
            keyboardController?.hide()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
            .padding(MaterialTheme.spacing.extraSmall)
            .imePadding()
            .clickable { onDismiss() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {


        FontSelector(
            selectedFont = selectedFont,
            onFontSelected = { fontFamily ->
                onSelectFontFamily(fontFamily)
                focusRequester.requestFocus()

            }
        )

        QuickColorPicker(
            selectedColor = selectedColor,
            onColorSelected = { color ->
                onSelectColor(color)
            },
            onOpenCustomColorPicker = {
                onShowColorPicker()
            },
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
                    IconButton(onClick = { onConfirm(text) }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onConfirm(text) }
            )
        )
    }


}