package com.an.facefilters

import android.app.Application
import com.an.core_editor.di.coreModule
import com.an.facefilters.di.appModule
import com.an.facefilters.di.useCaseModule
import com.an.feature_canvas.di.canvasModule
import com.an.feature_text.di.textModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FaceApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FaceApp)
            modules(
                appModule,
                useCaseModule,
                coreModule,
                canvasModule,
                textModule
            )
        }
    }
}