package com.an.feature_stickers.data

import android.content.Context
import android.graphics.Bitmap
import com.an.core_editor.domain.model.Point
import com.an.feature_stickers.domain.StickerCategory
import com.an.feature_stickers.domain.StickerManager
import java.io.File
import java.util.UUID

class StickerManagerImpl(
    private val context: Context
): StickerManager {

    companion object {
        const val USER_STICKER = "user_sticker"
        const val BASE_PATH = "stickers"
    }


    override suspend fun loadCategories(): List<String> {
        val userStickers = loadUserStickers()
        val categories = mutableListOf<String>()
        if(userStickers.isNotEmpty()) categories.add("yours")

        val stickersDirs = context.assets.list(BASE_PATH)?.toList().orEmpty()

        categories.addAll(stickersDirs)

        return categories.toList()
    }

    override suspend fun loadStickersByCategory(category: StickerCategory): List<String> {
        val path = "$BASE_PATH/${category.name.lowercase()}"
        return context
            .assets
            .list(path)
            ?.toList()
            .orEmpty()
    }

    override suspend fun loadUserStickers(): List<String> {
        val dir = File(context.filesDir, USER_STICKER)
        return dir.listFiles()
            ?.filter {
                it.extension == "png"
            }
            ?.map { file ->
                file.path
            } ?: emptyList()
    }

    override suspend fun createNewSticker(bitmap: Bitmap) {
/*
        val dir = File(context.filesDir, USER_STICKER)
        if(!dir.exists()) dir.mkdirs()

        val filename = "${UUID.randomUUID()}.png"
        val file = File(dir, filename)

        file.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }*/
    }

    override suspend fun getSticker(stickerPath: String) {

    }

}