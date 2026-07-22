package com.an.feature_image_caching

import org.koin.dsl.module

val imageCachingModule = module {
    single <BitmapSaver> { BitmapSaverImpl(get()) }
    single <BitmapCache> { BitmapCache(get()) }
}