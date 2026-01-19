package com.an.facefilters

import android.app.Application
import com.an.core_editor.di.coreModule
import com.an.feature_canvas.di.canvasModule
import com.an.feature_drawing.di.drawingModule
import com.an.feature_image_editing.di.imageEditingModule
import com.an.feature_stickers.di.stickerModule
import com.an.feature_text.di.textModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FaceApp: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@FaceApp)
            modules(
                coreModule,
                canvasModule,
                textModule,
                stickerModule,
                imageEditingModule,
                //savingModule,
                drawingModule
            )
        }
    }
}