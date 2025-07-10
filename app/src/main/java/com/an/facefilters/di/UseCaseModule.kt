package com.an.facefilters.di


import com.an.facefilters.canvas.domain.use_cases.editing.ApplyFilter
import com.an.facefilters.canvas.domain.use_cases.editing.ChangeElementAlpha
import com.an.facefilters.canvas.domain.use_cases.editing.CropImage
import com.an.facefilters.canvas.domain.use_cases.editing.EditingUseCases
import com.an.facefilters.canvas.domain.use_cases.editing.RemoveBackground
import com.an.facefilters.canvas.domain.use_cases.SelectFontFamily
import com.an.facefilters.canvas.domain.use_cases.editing.TransformElement
import com.an.facefilters.canvas.domain.use_cases.elements.UpdateElementsOrder
import org.koin.dsl.module

val useCaseModule = module {

    // Editing
    factory { RemoveBackground(get()) }
    factory { ApplyFilter() }
    factory { CropImage() }
    factory { TransformElement() }
    factory { ChangeElementAlpha() }

    factory { EditingUseCases(get(), get(), get(), get(), get(),) }



    factory { SelectFontFamily() }
    factory { UpdateElementsOrder() }


}