package com.an.feature_image_editing.di

import com.an.feature_image_editing.data.SubjectDetectorImpl
import com.an.feature_image_editing.domain.SubjectDetector
import com.an.feature_image_editing.domain.use_cases.ApplyFilter
import com.an.feature_image_editing.domain.use_cases.ChangeElementAlpha
import com.an.feature_image_editing.domain.use_cases.CropImage
import com.an.feature_image_editing.domain.use_cases.DeleteImage
import com.an.feature_image_editing.domain.use_cases.EditingUseCases
import com.an.feature_image_editing.domain.use_cases.RemoveBackground
import com.an.feature_image_editing.domain.use_cases.SaveDrawings
import com.an.feature_image_editing.presentation.ImageEditingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val imageEditingModule = module {
    viewModel { ImageEditingViewModel(get(), get(), get()) }

    factory<SubjectDetector> { SubjectDetectorImpl() }

    factory { ApplyFilter(get(), get()) }
    factory { CropImage(get(), get()) }
    factory { DeleteImage(get()) }
    factory { RemoveBackground(get(), get(), get()) }
    factory { ChangeElementAlpha(get()) }
    factory { SaveDrawings(get(), get()) }

    factory { EditingUseCases(get(), get(), get(), get(), get(), get()) }

}