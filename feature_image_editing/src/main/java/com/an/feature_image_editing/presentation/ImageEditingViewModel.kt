package com.an.feature_image_editing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.an.core_editor.presentation.mappers.toOffset
import com.an.core_editor.presentation.model.UiImageModel
import com.an.core_project.domain.ProjectEditor
import com.an.feature_image_caching.BitmapCache
import com.an.feature_image_editing.domain.use_cases.EditingUseCases
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ImageEditingViewModel(
    private val bitmapCache: BitmapCache,
    private val projectEditor: ProjectEditor,
    private val useCases: EditingUseCases,
): ViewModel() {


    val editedImage = projectEditor
            .selectedImage
            .map { domainImage ->
                if(domainImage == null) {
                    null
                } else  {
                    UiImageModel(
                        rotationAngle = domainImage.rotationAngle,
                        scale = domainImage.scale,
                        alpha = domainImage.alpha,
                        position = domainImage.position.toOffset(),
                        bitmap = bitmapCache.getEditedBitmap(domainImage.imagePath),
                        currentFilter = domainImage.currentFilter,
                    )
                }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )


    private val _events = Channel<EditingEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun onAction(action: ImageEditingAction) {
        when(action) {
            is EditingAction -> handleEditingAction(action)
        }

    }


    private fun handleEditingAction(action: EditingAction) {
        viewModelScope.launch {
            when(action) {
                is EditingAction.ApplyFilter -> useCases.applyFilter(
                    filter = action.filter,
                )
                is EditingAction.ChangeElementAlpha -> useCases.changeElementAlpha(
                    newAlpha = action.alpha
                )
                is EditingAction.CropImage -> {
                    useCases.cropImage(
                        left = action.srcRect.left,
                        top = action.srcRect.top,
                        width = action.srcRect.width,
                        height = action.srcRect.height,
                    )
                    _events.send(EditingEvent.PopBackStack)
                }
                EditingAction.RemoveBackground -> useCases.removeBackground()
                EditingAction.DeleteImage -> {
                }
                EditingAction.CancelCropping -> _events.send(EditingEvent.PopBackStack)
            }

        }
    }



}