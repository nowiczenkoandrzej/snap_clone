package com.an.core_project.domain

import android.graphics.Bitmap
import android.util.Log
import com.an.feature_image_caching.BitmapSaver
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ProjectThumbnail(
    val id: Long,
    val lastChange: LocalDateTime,
    val graphic: Bitmap?
)

suspend fun ProjectSummary.toThumbnail(
    bitmapSaver: BitmapSaver
): ProjectThumbnail {



    val thumbnail = if(this.thumbNail == "")
        null
    else
        bitmapSaver.loadBitmap(this.thumbNail)
    Log.d("TAG", "toThumbnail: ${this.thumbNail}, $thumbnail")

    return ProjectThumbnail(
        id = this.id,
        lastChange = LocalDateTime.ofEpochSecond(
            this.lastChange,
            0,
            ZoneOffset.UTC
        ),
        graphic = thumbnail
    )
}
