package com.an.facefilters.canvas.domain.use_cases.elements

import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class UpdateElementsOrderTest {

    private lateinit var updateElementsOrder: UpdateElementsOrder
    private lateinit var mockElement1: Element
    private lateinit var mockElement2: Element
    private lateinit var mockElement3: Element
    private lateinit var mockElement4: Element

    @Before
    fun setUp() {
        updateElementsOrder = UpdateElementsOrder()
        mockElement1 = mock(Element::class.java).apply {
            `when`(toString()).thenReturn("Element1")
        }
        mockElement2 = mock(Element::class.java).apply {
            `when`(toString()).thenReturn("Element2")
        }
        mockElement3 = mock(Element::class.java).apply {
            `when`(toString()).thenReturn("Element3")
        }
        mockElement4 = mock(Element::class.java).apply {
            `when`(toString()).thenReturn("Element4")
        }
    }

    @Test
    fun `move element forward in list`() {
        val initialList = listOf(mockElement1, mockElement2, mockElement3, mockElement4)

        val result = updateElementsOrder.invoke(
            list = initialList,
            from = 1,
            to = 3
        )

        assertTrue(result is Result.Success)
        val reorderedList = (result as Result.Success).data
        assertEquals(listOf(mockElement1, mockElement3, mockElement4, mockElement2), reorderedList)
    }

    @Test
    fun `move element backward in list`() {
        val initialList = listOf(mockElement1, mockElement2, mockElement3, mockElement4)

        val result = updateElementsOrder.invoke(
            list = initialList,
            from = 2,
            to = 0
        )

        assertTrue(result is Result.Success)
        val reorderedList = (result as Result.Success).data
        assertEquals(listOf(mockElement3, mockElement1, mockElement2, mockElement4), reorderedList)
    }

    @Test
    fun `move element to same position returns original list`() {
        val initialList = listOf(mockElement1, mockElement2, mockElement3)

        val result = updateElementsOrder.invoke(
            list = initialList,
            from = 1,
            to = 1
        )

        assertTrue(result is Result.Success)
        assertEquals(initialList, (result as Result.Success).data)
    }

    @Test
    fun `invalid from index returns failure`() {
        val initialList = listOf(mockElement1, mockElement2)

        val result1 = updateElementsOrder.invoke(initialList, from = -1, to = 0)
        val result2 = updateElementsOrder.invoke(initialList, from = 2, to = 0)

        assertTrue(result1 is Result.Failure)
        assertTrue(result2 is Result.Failure)
        assertEquals("Element not Found", (result1 as Result.Failure).message)
        assertEquals("Element not Found", (result2 as Result.Failure).message)
    }

    @Test
    fun `invalid to index returns failure`() {
        val initialList = listOf(mockElement1, mockElement2)

        val result1 = updateElementsOrder.invoke(initialList, from = 0, to = -1)
        val result2 = updateElementsOrder.invoke(initialList, from = 0, to = 2)

        assertTrue(result1 is Result.Failure)
        assertTrue(result2 is Result.Failure)
        assertEquals("Element not Found", (result1 as Result.Failure).message)
        assertEquals("Element not Found", (result2 as Result.Failure).message)
    }

    @Test
    fun `original list remains unmodified`() {
        val initialList = listOf(mockElement1, mockElement2, mockElement3)

        updateElementsOrder.invoke(
            list = initialList,
            from = 0,
            to = 2
        )

        assertEquals(3, initialList.size)
        assertEquals(listOf(mockElement1, mockElement2, mockElement3), initialList)
    }

    @Test
    fun `move first element to last position`() {
        val initialList = listOf(mockElement1, mockElement2, mockElement3)

        val result = updateElementsOrder.invoke(
            list = initialList,
            from = 0,
            to = 2
        )

        assertTrue(result is Result.Success)
        assertEquals(listOf(mockElement2, mockElement3, mockElement1), (result as Result.Success).data)
    }

    @Test
    fun `move last element to first position`() {
        val initialList = listOf(mockElement1, mockElement2, mockElement3)

        val result = updateElementsOrder.invoke(
            list = initialList,
            from = 2,
            to = 0
        )

        assertTrue(result is Result.Success)
        assertEquals(listOf(mockElement3, mockElement1, mockElement2), (result as Result.Success).data)
    }

    @Test
    fun `single element list returns same list`() {
        val singleElementList = listOf(mockElement1)

        val result = updateElementsOrder.invoke(
            list = singleElementList,
            from = 0,
            to = 0
        )

        assertTrue(result is Result.Success)
        assertEquals(singleElementList, (result as Result.Success).data)
    }
}