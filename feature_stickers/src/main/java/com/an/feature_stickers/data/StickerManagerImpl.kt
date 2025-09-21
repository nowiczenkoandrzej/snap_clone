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
        val categories = mutableListOf<String>()

        val stickersDirs = context.assets.list(BASE_PATH)?.toList().orEmpty()

        categories.addAll(stickersDirs)

        return categories.toList()
    }

    override suspend fun loadStickersByCategory(category: String): List<String> {
        val path = "$BASE_PATH/${category.lowercase()}"
        return context
            .assets
            .list(path)
            ?.toList()
            .orEmpty()
    }




    override suspend fun getSticker(stickerPath: String) {

    }

    override suspend fun loadStickersMap(): Map<String, List<String>> {

        val dir = File(context.filesDir, USER_STICKER)

        return mapOf(
            "Activities" to context.assets.list("$BASE_PATH/activities")?.toList().orEmpty(),
            "Animals" to context.assets.list("$BASE_PATH/animals")?.toList().orEmpty(),
            "Clothing" to context.assets.list("$BASE_PATH/clothing")?.toList().orEmpty(),
            "Emojis" to context.assets.list("$BASE_PATH/emojis")?.toList().orEmpty(),
            "Food" to context.assets.list("$BASE_PATH/food")?.toList().orEmpty(),
            "Music" to context.assets.list("$BASE_PATH/music")?.toList().orEmpty(),
            "Objects" to context.assets.list("$BASE_PATH/objects")?.toList().orEmpty()
        )
    }

}