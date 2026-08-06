package com.an.core_project.domain

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.time.LocalDateTime
import java.time.ZoneOffset

data class ProjectThumbnail(
    val id: Long,
    val lastChange: LocalDateTime,
    val graphic: Bitmap?
)

fun ProjectSummary.toThumbnail(): ProjectThumbnail {
    Log.d("TAG", "toThumbnail: ${this.thumbNail}")
    return ProjectThumbnail(
        id = this.id,
        lastChange = LocalDateTime.ofEpochSecond(
            this.lastChange,
            0,
            ZoneOffset.UTC
        ),
        graphic = BitmapFactory.decodeFile(this.thumbNail)
    )
}
