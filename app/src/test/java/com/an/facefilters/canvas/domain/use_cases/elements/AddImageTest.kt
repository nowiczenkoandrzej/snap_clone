package com.an.facefilters.canvas.domain.use_cases.elements

import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

class AddImageTest {

    private lateinit var addImage: AddImage
    private lateinit var mockImg: Img
    private lateinit var mockElement: Element

    @Before
    fun setUp() {
        addImage = AddImage()
        mockImg = mock(Img::class.java)
        mockElement = mock(Element::class.java)
    }

    @Test
    fun `add null image returns failure`() {
        val testList = listOf(mockElement)

        val result = addImage.invoke(
            list = testList,
            img = null
        )

        assertTrue(result is Result.Failure)
        assertEquals("Element not found.", (result as Result.Failure).message)
    }

    @Test
    fun `add image to empty list returns success with single element`() {
        val emptyList = emptyList<Element>()

        val result = addImage.invoke(
            list = emptyList,
            img = mockImg
        )

        assertTrue(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.size)
        assertTrue(result.data.contains(mockImg))
    }

    @Test
    fun `add image to non-empty list returns success with added element`() {
        val initialList = listOf(mockElement)

        val result = addImage.invoke(
            list = initialList,
            img = mockImg
        )

        assertTrue(result is Result.Success)
        val resultList = (result as Result.Success).data
        assertEquals(2, resultList.size)
        assertTrue(resultList.containsAll(listOf(mockElement, mockImg)))
    }

    @Test
    fun `verify original list is not modified`() {
        val initialList = listOf(mockElement)

        addImage.invoke(
            list = initialList,
            img = mockImg
        )

        assertEquals(1, initialList.size)
    }

    @Test
    fun `add multiple images sequentially`() {
        val mockImg2 = mock(Img::class.java)
        val emptyList = emptyList<Element>()

        val result1 = addImage.invoke(emptyList, mockImg)
        val result2 = addImage.invoke((result1 as Result.Success).data, mockImg2)

        assertTrue(result2 is Result.Success)
        assertEquals(2, (result2 as Result.Success).data.size)
        assertTrue(result2.data.containsAll(listOf(mockImg, mockImg2)))
    }
}