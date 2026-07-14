package com.an.feature_image_rendering

import org.koin.dsl.module

val imageRenderingModule = module {

    single <ImageRenderer> { ImageRendererImpl(get()) }
}