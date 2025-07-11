package com.an.facefilters.di


import com.an.facefilters.canvas.domain.use_cases.editing.ApplyFilter
import com.an.facefilters.canvas.domain.use_cases.editing.ChangeElementAlpha
import com.an.facefilters.canvas.domain.use_cases.editing.CropImage
import com.an.facefilters.canvas.domain.use_cases.editing.EditingUseCases
import com.an.facefilters.canvas.domain.use_cases.editing.RemoveBackground
import com.an.facefilters.canvas.domain.use_cases.elements.SelectFontFamily
import com.an.facefilters.canvas.domain.use_cases.editing.TransformElement
import com.an.facefilters.canvas.domain.use_cases.elements.AddImage
import com.an.facefilters.canvas.domain.use_cases.elements.AddText
import com.an.facefilters.canvas.domain.use_cases.elements.DeleteElement
import com.an.facefilters.canvas.domain.use_cases.elements.ElementsUseCases
import com.an.facefilters.canvas.domain.use_cases.elements.UpdateElementsOrder
import com.an.facefilters.canvas.domain.use_cases.stickers.CreateNewSticker
import com.an.facefilters.canvas.domain.use_cases.stickers.LoadSticker
import com.an.facefilters.canvas.domain.use_cases.stickers.LoadStickerByCategory
import com.an.facefilters.canvas.domain.use_cases.stickers.LoadStickerState
import com.an.facefilters.canvas.domain.use_cases.stickers.StickersUseCases
import org.koin.dsl.module

val useCaseModule = module {

    // Editing
    factory { RemoveBackground(get()) }
    factory { ApplyFilter() }
    factory { CropImage() }
    factory { TransformElement() }
    factory { ChangeElementAlpha() }

    factory { EditingUseCases(get(), get(), get(), get(), get(),) }


    // Elements

    factory { AddImage() }
    factory { AddText() }
    factory { DeleteElement() }
    factory { SelectFontFamily() }
    factory { UpdateElementsOrder() }

    factory { ElementsUseCases(get(), get(), get(), get(), get()) }


    // Stickers

    factory { CreateNewSticker(get(), get()) }
    factory { LoadSticker(get()) }
    factory { LoadStickerByCategory(get()) }
    factory { LoadStickerState(get()) }

    factory { StickersUseCases(get(), get(), get(), get()) }


}