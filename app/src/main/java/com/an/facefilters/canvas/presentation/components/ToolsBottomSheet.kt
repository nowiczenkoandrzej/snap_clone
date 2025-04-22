package com.an.facefilters.canvas.presentation.components

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.an.facefilters.R
import com.an.facefilters.canvas.domain.model.Tool
import com.an.facefilters.canvas.domain.model.Tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsBottomSheet(
    onToolSelected: (Tools) -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope
) {
    val context = LocalContext.current

    val tools = listOf(
        Tool(
            type = Tools.AddPhoto,
            name = context.getString(R.string.add_image)
        ),
    )

    ModalBottomSheet(
        onDismissRequest =  { onDismiss() },
        sheetState = sheetState
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(4)
        ) {
            items(tools) { tool ->
                TextButton(
                    onClick = {
                        onToolSelected(tool.type)
                        scope.launch {
                            sheetState.hide()
                        }
                    }
                ) {
                    Text(tool.name)
                }
            }
        }

    }

}