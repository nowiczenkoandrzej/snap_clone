package com.an.feature_image_editing.domain.use_cases

import android.graphics.Bitmap
import com.an.core_editor.data.BitmapCache
import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.Result
import com.an.feature_image_editing.domain.SubjectDetector
import kotlinx.coroutines.delay

class RemoveBackground(
    private val subjectDetector: SubjectDetector,
    private val bitmapCache: BitmapCache,
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(): Result<Unit> {

        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val operatedBitmap = bitmapCache.getEdited(editedElement.image.path)
            ?: return Result.Failure("Something went wrong")

        var result: Result<Bitmap>? = null
        subjectDetector.detectSubject(
            bitmap = operatedBitmap,
            onSubjectDetected = { bitmap ->

                result = Result.Success(bitmap)
            },
            onError = { message ->
                result = Result.Failure(message)
            }
        )
        while(result == null) {
            delay(100)
        }


        val res = (result as Result<Bitmap>)
        if(res is Result.Success) {
            bitmapCache.updateEdited(editedElement.image.path, res.data)
            editorRepository.updateElement(
                index = editorRepository.state.value.selectedElementIndex!!,
                newElement = editedElement.copy(
                    version = System.currentTimeMillis()
                ),
                saveUndo = true
            )
            return Result.Success(Unit)
        } else {
            return Result.Failure("Something went wrong")
        }

    }

}