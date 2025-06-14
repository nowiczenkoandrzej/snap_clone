package com.an.facefilters.canvas.domain.use_cases

class CanvasUseCaseProvider(
    val detectSubject: DetectSubject,
    val deleteElement: DeleteElement,
    val cropImage: CropImage,
    val selectFontFamily: SelectFontFamily,
    val updateElementsOrder: UpdateElementsOrder
)