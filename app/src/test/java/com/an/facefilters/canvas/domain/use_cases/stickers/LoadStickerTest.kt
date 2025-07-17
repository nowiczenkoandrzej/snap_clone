package com.an.facefilters.canvas.domain.use_cases.stickers

import android.graphics.Bitmap
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

class LoadStickerTest {

    private lateinit var loadSticker: LoadSticker
    private lateinit var mockStickerManager: StickerManager
    private lateinit var mockBitmap: Bitmap

    @Before
    fun setUp() {
        mockStickerManager = mock(StickerManager::class.java)
        mockBitmap = mock(Bitmap::class.java)
        loadSticker = LoadSticker(mockStickerManager)
    }

    @Test
    fun `load sticker successfully returns bitmap`(): Unit = runBlocking {
        val testPath = "test/path.png"
        `when`(mockStickerManager.loadPngAsBitmap(testPath)).thenReturn(mockBitmap)

        val result = loadSticker.invoke(testPath)

        assertTrue(result is Result.Success)
        assertEquals(mockBitmap, (result as Result.Success).data)
        verify(mockStickerManager).loadPngAsBitmap(testPath)
    }


    @Test
    fun `verify sticker manager is called exactly once`(): Unit = runBlocking {
        val testPath = "test/path.png"
        `when`(mockStickerManager.loadPngAsBitmap(testPath)).thenReturn(mockBitmap)

        loadSticker.invoke(testPath)

        verify(mockStickerManager).loadPngAsBitmap(testPath)
    }


    @Test
    fun `security exception returns failure with message`() = runBlocking {
        val restrictedPath = "restricted/path.png"
        `when`(mockStickerManager.loadPngAsBitmap(restrictedPath))
            .thenThrow(SecurityException("Permission denied"))

        val result = loadSticker.invoke(restrictedPath)

        assertTrue(result is Result.Failure)
        assertEquals("Permission denied", (result as Result.Failure).message)
    }
}