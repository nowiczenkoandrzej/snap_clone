package com.an.feature_text.presentation

import androidx.activity.compose.BackHandler
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import com.an.core_ui.ui.theme.spacing
import com.an.feature_text.presentation.components.ColorPicker
import com.an.feature_text.presentation.components.FontSelector
import com.an.feature_text.presentation.components.QuickColorPicker
import kotlinx.coroutines.launch

@Composable
fun AddTextScreen(
    viewModel: TextViewModel,
    popBackStack: () -> Unit
) {

    val textState = viewModel
        .textState
        .collectAsState()
        .value

    val focusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current


    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(textState.showColorPicker){
        if(!textState.showColorPicker) {
            keyboardController?.show()
        } else {
            keyboardController?.hide()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when(event) {
                TextEvent.PopNavStack -> popBackStack()
                is TextEvent.ShowSnackbar -> scope.launch {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    BackHandler {
        popBackStack()
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { contentPadding ->

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                    .padding(contentPadding)
                    .imePadding()
                    .clickable {

                    },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {


                FontSelector(
                    selectedFont = textState.fontItem,
                    onFontSelected = {
                        viewModel.onAction(TextAction.ApplyFontFamily(it))
                        focusRequester.requestFocus()
                    }
                )

                QuickColorPicker(
                    selectedColor = textState.textColor,
                    onColorSelected = { viewModel.onAction(TextAction.ApplyTextColor(it)) },
                    onOpenCustomColorPicker = { viewModel.onAction(TextAction.ToggleColorPicker) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = textState.currentText,
                    onValueChange = { viewModel.onAction(TextAction.Type(it)) },
                    modifier = Modifier.focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontFamily = textState.fontItem.fontFamily,
                        color = Color.White,
                    ),
                    trailingIcon = {
                        if(textState.currentText.isNotBlank()) {
                            IconButton(onClick = { viewModel.onAction(TextAction.AddText) }) {
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
                        onDone = { viewModel.onAction(TextAction.AddText) }
                    )
                )
            }

            if(textState.showColorPicker) {
                ColorPicker(
                    modifier = Modifier.fillMaxSize(),
                    onColorSelected = { color ->
                        viewModel.onAction(TextAction.ApplyTextColor(color))
                    }
                )
            }

        }

    }


}