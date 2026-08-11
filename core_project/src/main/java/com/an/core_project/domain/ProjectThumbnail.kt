package com.an.core_project.domain

import android.graphics.Bitmap
import com.an.core_project.presentation.formatLastEdited
import com.an.feature_image_caching.BitmapSaver

data class ProjectThumbnail(
    val id: Long,
    val lastChange: String,
    val graphic: Bitmap?
)

suspend fun ProjectSummary.toThumbnail(
    bitmapSaver: BitmapSaver
): ProjectThumbnail {



    val thumbnail = if(this.thumbNail == "")
        null
    else
        bitmapSaver.loadBitmap(this.thumbNail)


    return ProjectThumbnail(
        id = this.id,
        lastChange = formatLastEdited(this.lastChange),
        graphic = thumbnail
    )
}
