package com.an.facefilters.canvas.domain.use_cases.editing

class EditingUseCases(
    val applyFilter: ApplyFilter,
    val changeElementAlpha: ChangeElementAlpha,
    val cropImage: CropImage,
    val removeBackground: RemoveBackground,
    val transformElement: TransformElement
)
