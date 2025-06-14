package com.an.facefilters.di

import com.an.facefilters.canvas.data.SubjectDetector
import com.an.facefilters.canvas.domain.use_cases.CanvasUseCaseProvider
import com.an.facefilters.canvas.domain.use_cases.CropImage
import com.an.facefilters.canvas.domain.use_cases.DeleteElement
import com.an.facefilters.canvas.domain.use_cases.DetectSubject
import com.an.facefilters.canvas.domain.use_cases.SelectFontFamily
import com.an.facefilters.canvas.domain.use_cases.UpdateElementsOrder
import org.koin.dsl.module

val useCaseModule = module {
    factory { DetectSubject(get()) }
    factory { DeleteElement() }
    factory { CropImage() }
    factory { SelectFontFamily() }
    factory { UpdateElementsOrder() }

    factory { CanvasUseCaseProvider(get(), get(), get(), get(), get()) }
}