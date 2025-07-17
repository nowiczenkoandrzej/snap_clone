package com.an.facefilters.canvas.domain.use_cases.stickers

import com.an.facefilters.canvas.domain.managers.StickerCategory
import com.an.facefilters.canvas.domain.managers.StickerManager
import com.an.facefilters.canvas.domain.model.Result
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify
import java.io.IOException

class LoadStickerByCategoryTest {

    private lateinit var loadStickerByCategory: LoadStickerByCategory
    private lateinit var mockStickerManager: StickerManager
    private val testCategory = StickerCategory.ANIMALS
    private val testStickers = listOf("sticker1.png", "sticker2.png", "sticker3.png")

    @Before
    fun setUp() {
        mockStickerManager = mock(StickerManager::class.java)
        loadStickerByCategory = LoadStickerByCategory(mockStickerManager)
    }

    @Test
    fun `load stickers by category successfully returns list`(): Unit = runBlocking {
        `when`(mockStickerManager.loadStickersByCategory(testCategory)).thenReturn(testStickers)

        val result = loadStickerByCategory.invoke(testCategory)

        assertTrue(result is Result.Success)
        assertEquals(testStickers, (result as Result.Success).data)
        verify(mockStickerManager).loadStickersByCategory(testCategory)
    }


    @Test
    fun `load stickers with security exception returns failure`() = runBlocking {
        val errorMessage = "Permission denied"
        `when`(mockStickerManager.loadStickersByCategory(testCategory))
            .thenThrow(SecurityException(errorMessage))

        val result = loadStickerByCategory.invoke(testCategory)

        assertTrue(result is Result.Failure)
        assertEquals(errorMessage, (result as Result.Failure).message)
    }

    @Test
    fun `verify sticker manager is called exactly once`(): Unit = runBlocking {
        `when`(mockStickerManager.loadStickersByCategory(testCategory)).thenReturn(testStickers)

        loadStickerByCategory.invoke(testCategory)

        verify(mockStickerManager).loadStickersByCategory(testCategory)
    }
    
}