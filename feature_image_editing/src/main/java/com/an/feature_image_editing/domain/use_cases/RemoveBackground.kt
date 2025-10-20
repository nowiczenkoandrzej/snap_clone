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

    suspend operator fun invoke(): Result<BooleanArray> {

        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")

        if(editedElement !is DomainImageModel)
            return Result.Failure("Couldn't find element")

        val operatedBitmap = bitmapCache.get(editedElement.imagePath)
            ?: return Result.Failure("Something went wrong")

        var result: Result<BooleanArray>? = null
        subjectDetector.detectSubject(
            bitmap = operatedBitmap,
            onSubjectDetected = { array ->

                result = Result.Success(array)

            },
            onError = { message ->
                result = Result.Failure(message)
            }
        )
        while(result == null) {
            delay(100)
        }

        val mask = (result as Result.Success<BooleanArray>).data

        editorRepository.updateElement(
            newElement = editedElement.copy(
                subjectMask = mask,
                version = System.currentTimeMillis()
            ),
            index = editorRepository.state.value.selectedElementIndex!!
        )

        return result as Result<BooleanArray>

    }

}