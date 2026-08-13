package com.an.feature_image_editing.domain.use_cases

import android.graphics.BitmapFactory
import com.an.core_editor.domain.DomainImageEdit
import com.an.core_editor.domain.model.Result
import com.an.core_project.domain.ProjectEditor
import com.an.feature_image_editing.domain.SubjectDetector
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

class RemoveBackground(
    private val subjectDetector: SubjectDetector,
    private val projectEditor: ProjectEditor
) {

    suspend operator fun invoke(): Result<BooleanArray> {

        val editedElement = projectEditor.selectedImage.first()
            ?: return Result.Failure("Couldn't find element")

        val operatedBitmap = BitmapFactory.decodeFile(editedElement.imagePath)
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

        val newEditList = editedElement.edits + DomainImageEdit.RemoveBackground(mask)


        projectEditor.updateElement(
            newElement = editedElement.copy(
                edits = newEditList,
                version = System.currentTimeMillis()
            ),
        )

        return result as Result<BooleanArray>

    }

}