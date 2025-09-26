package com.an.feature_image_editing.domain.use_cases

class EditingUseCases(
    val applyFilter: ApplyFilter,
    val changeElementAlpha: ChangeElementAlpha,
    val cropImage: CropImage,
    val deleteImage: DeleteImage,
    val removeBackground: RemoveBackground,
    val saveDrawings: SaveDrawings,
    val applyRubber: ApplyRubber,
    val erasePathFromBitmap: ErasePathFromBitmap
)