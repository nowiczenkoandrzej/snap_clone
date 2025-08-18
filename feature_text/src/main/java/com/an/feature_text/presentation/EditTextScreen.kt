package com.an.feature_text.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.traceEventStart
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.an.core_editor.domain.model.DomainTextModel
import com.an.core_editor.presentation.toUiTextModel
import com.an.feature_text.presentation.components.ColorPicker
import com.an.feature_text.presentation.components.FontSelector
import com.an.feature_text.presentation.components.QuickColorPicker
import kotlinx.coroutines.launch

@Composable
fun EditTextScreen(
    viewModel: TextViewModel,
    popBackStack: () -> Unit
) {



    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val editorState = viewModel
        .editorState
        .collectAsState()
        .value

    val textState = viewModel
        .textState
        .collectAsState()
        .value


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

        val index = editorState.selectedElementIndex

        if(index == null) {
            popBackStack()
        }

        val editedElement = editorState.elements[index!!]

        if(editedElement !is DomainTextModel) {
            popBackStack()
        }

        val editedText = (editedElement as DomainTextModel).toUiTextModel()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Column(
                    modifier = Modifier
                        .weight(5f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {



                    Text(
                        text = editedText.text,
                        fontFamily = editedText.fontItem.fontFamily,
                        color = editedText.fontColor,
                        fontSize = editedText.fontSize.sp
                    )

                }

                Column(
                    modifier = Modifier
                        .weight(2f)
                ) {
                    FontSelector(
                        modifier = Modifier.fillMaxWidth(),
                        selectedFont = editedText.fontItem,
                        onFontSelected = { fontItem ->
                            viewModel.onAction(TextAction.ChangeFontFamily(fontItem))
                        }
                    )
                    QuickColorPicker(
                        selectedColor = editedText.fontColor,
                        onColorSelected = { color ->
                            viewModel.onAction(TextAction.ChangeFontColor(color))
                        },
                        onOpenCustomColorPicker = {
                            viewModel.onAction(TextAction.ToggleColorPicker)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if(textState.showColorPicker) {
                ColorPicker(
                    modifier = Modifier.fillMaxSize(),
                    onColorSelected = { color ->
                        viewModel.onAction(TextAction.ChangeFontColor(color))
                    }
                )
            }
        }

    }

}