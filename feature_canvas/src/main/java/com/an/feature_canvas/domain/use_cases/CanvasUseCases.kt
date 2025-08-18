package com.an.feature_canvas.domain.use_cases

class CanvasUseCases(
    val addImage: AddImage,
    val deleteElement: DeleteElement,
    val reorderElements: ReorderElements,
    val selectElement: SelectElement,
    val transformElement: TransformElement,
    val undo: Undo
)