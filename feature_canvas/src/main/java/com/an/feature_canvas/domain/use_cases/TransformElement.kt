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

        val state = editorRepository.state.value

        if(state.selectedElementIndex == null) return Result.Failure("Element not Found")

        if(state.selectedElementIndex!! >= state.elements.size) return Result.Failure("Element not Found")
        if(state.selectedElementIndex!! < 0) return Result.Failure("Element not Found")


        val element = state.elements[state.selectedElementIndex!!]

        editorRepository.updateElement(
            index = state.selectedElementIndex!!,
            newElement = element.transform(
                scaleDelta = scaleDelta,
                rotationDelta = rotationDelta,
                translation = translation
            ),
            saveUndo = saveUndo
        )

        return Result.Success(Unit)
    }
}