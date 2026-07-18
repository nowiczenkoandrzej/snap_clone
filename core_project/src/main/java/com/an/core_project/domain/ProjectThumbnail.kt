package com.an.core_project.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ProjectThumbnail(
    val id: Long,
    val lastChange: LocalDateTime,
    val graphic: Bitmap
)

fun ProjectSummary.toThumbnail(): ProjectThumbnail {
    return ProjectThumbnail(
        id = this.id,
        lastChange = LocalDateTime.ofEpochSecond(
            this.lastChange,
            0,
            ZoneOffset.systemDefault() as ZoneOffset?
        ),
        graphic = BitmapFactory.decodeFile(this.thumbNail)
    )
}
