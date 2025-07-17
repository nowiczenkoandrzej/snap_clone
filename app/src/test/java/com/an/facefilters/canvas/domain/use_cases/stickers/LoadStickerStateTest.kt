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
import java.io.File


class LoadStickerStateTest {

    private lateinit var loadStickerState: LoadStickerState
    private lateinit var mockStickerManager: StickerManager

    private val testCategories = listOf(
        "activities",
        "animals",
        "food"
    )

    private val fakeFile = File("user1.png")
    private val fakeFile1 = File("user2.png")
    private val testStickers = listOf("sticker1.png", "sticker2.png")
    private val testUserStickers = listOf(fakeFile, fakeFile1)

    @Before
    fun setUp() {
        mockStickerManager = mock(StickerManager::class.java)
        loadStickerState = LoadStickerState(mockStickerManager)

        runBlocking {
            `when`(mockStickerManager.loadCategories()).thenReturn(testCategories)
            `when`(mockStickerManager.loadStickersByCategory(StickerCategory.ACTIVITIES))
                .thenReturn(testStickers)

            `when`(mockStickerManager.loadUserStickers()).thenReturn(testUserStickers)
        }
    }

    @Test
    fun `load sticker state successfully returns all data`(): Unit = runBlocking {
        val result = loadStickerState.invoke()

        assertTrue(result is Result.Success)
        val state = (result as Result.Success).data
        assertEquals(testCategories, state.categories)
        assertEquals(testStickers, state.stickers)
        assertEquals(testUserStickers, state.userStickers)

        verify(mockStickerManager).loadCategories()
        verify(mockStickerManager).loadStickersByCategory(StickerCategory.ACTIVITIES)
        verify(mockStickerManager).loadUserStickers()
    }


    @Test
    fun `empty categories returns empty list in state`() = runBlocking {
        `when`(mockStickerManager.loadCategories()).thenReturn(emptyList())

        val result = loadStickerState.invoke()

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.categories.isEmpty())
    }

    @Test
    fun `empty stickers returns empty list in state`() = runBlocking {
        `when`(mockStickerManager.loadStickersByCategory(StickerCategory.ACTIVITIES))
            .thenReturn(emptyList())

        val result = loadStickerState.invoke()

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.stickers.isEmpty())
    }

    @Test
    fun `empty user stickers returns empty list in state`() = runBlocking {
        `when`(mockStickerManager.loadUserStickers()).thenReturn(emptyList())

        val result = loadStickerState.invoke()

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.userStickers.isEmpty())
    }

    @Test
    fun `security exception returns permission error`() = runBlocking {
        val errorMessage = "Permission denied"
        `when`(mockStickerManager.loadCategories())
            .thenThrow(SecurityException(errorMessage))

        val result = loadStickerState.invoke()

        assertTrue(result is Result.Failure)
        assertEquals(errorMessage, (result as Result.Failure).message)
    }

    @Test
    fun `verify all manager methods are called exactly once`(): Unit = runBlocking {
        loadStickerState.invoke()

        verify(mockStickerManager).loadCategories()
        verify(mockStickerManager).loadStickersByCategory(StickerCategory.ACTIVITIES)
        verify(mockStickerManager).loadUserStickers()
    }
}