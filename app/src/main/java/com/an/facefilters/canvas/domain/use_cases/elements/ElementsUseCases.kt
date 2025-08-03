package com.an.facefilters.canvas.domain.use_cases.elements

class ElementsUseCases(
    val addImage: AddImage,
    val addText: AddText,
    val deleteElement: DeleteElement,
    val applyTextStyle: ApplyTextStyle,
    val updateElementsOrder: UpdateElementsOrder,
    val setTextColor: SetTextColor
)