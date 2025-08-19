package com.an.feature_canvas.domain.use_cases


import com.an.core_editor.domain.EditorRepository
import com.an.core_editor.domain.model.Point
import com.an.core_editor.domain.model.Result


class TransformElement(
    private val editorRepository: EditorRepository
) {

    suspend operator fun invoke(
        scaleDelta: Float,
        rotationDelta: Float,
        translation: Point,
        saveUndo: Boolean
    ): Result<Unit> {

        val editedElement = editorRepository.getSelectedElement()
            ?: return Result.Failure("Couldn't find element")


        editorRepository.updateElement(
            index = editorRepository.state.value.selectedElementIndex!!,
            newElement = editedElement.transform(
                scaleDelta = scaleDelta,
                rotationDelta = rotationDelta,
                translation = translation
            ),
            saveUndo = saveUndo
        )

        return Result.Success(Unit)
    }
}