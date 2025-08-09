package com.an.facefilters.canvas.domain.use_cases.elements

import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class DeleteDomainElementTest {

    private lateinit var deleteElement: DeleteElement
    private lateinit var mockElement1: Element
    private lateinit var mockElement2: Element
    private lateinit var mockElement3: Element

    @Before
    fun setUp() {
        deleteElement = DeleteElement()
        mockElement1 = mock(Element::class.java).apply {
            `when`(this.toString()).thenReturn("Element1")
        }
        mockElement2 = mock(Element::class.java).apply {
            `when`(this.toString()).thenReturn("Element2")
        }
        mockElement3 = mock(Element::class.java).apply {
            `when`(this.toString()).thenReturn("Element3")
        }
    }

    @Test
    fun `delete element from empty list returns failure`() {
        val emptyList = emptyList<Element>()

        val result = deleteElement.invoke(
            list = emptyList,
            element = mockElement1
        )

        assertTrue(result is Result.Failure)
        assertEquals("Element not Found", (result as Result.Failure).message)
    }

    @Test
    fun `delete null element returns failure`() {
        val listWithElements = listOf(mockElement1, mockElement2)

        val result = deleteElement.invoke(
            list = listWithElements,
            element = null
        )

        assertTrue(result is Result.Failure)
        assertEquals("Element not Found", (result as Result.Failure).message)
    }

    @Test
    fun `delete non-existing element returns failure`() {
        val listWithElements = listOf(mockElement1, mockElement2)

        val result = deleteElement.invoke(
            list = listWithElements,
            element = mockElement3
        )

        assertTrue(result is Result.Failure)
        assertEquals("Element not Found", (result as Result.Failure).message)
    }

    @Test
    fun `delete existing element returns success with modified list`() {
        val listWithElements = listOf(mockElement1, mockElement2, mockElement3)

        val result = deleteElement.invoke(
            list = listWithElements,
            element = mockElement2
        )

        assertTrue(result is Result.Success)
        val resultList = (result as Result.Success).data
        assertEquals(2, resultList.size)
        assertTrue(resultList.containsAll(listOf(mockElement1, mockElement3)))
        assertFalse(resultList.contains(mockElement2))
    }

    @Test
    fun `delete only element returns empty list`() {
        val singleElementList = listOf(mockElement1)

        val result = deleteElement.invoke(
            list = singleElementList,
            element = mockElement1
        )

        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.isEmpty())
    }

    @Test
    fun `original list remains unmodified`() {
        val originalList = listOf(mockElement1, mockElement2)

        deleteElement.invoke(
            list = originalList,
            element = mockElement1
        )

        assertEquals(2, originalList.size)
        assertTrue(originalList.contains(mockElement1))
    }

    @Test
    fun `delete duplicate elements removes only one instance`() {
        val listWithDuplicates = listOf(mockElement1, mockElement2, mockElement1)

        val result = deleteElement.invoke(
            list = listWithDuplicates,
            element = mockElement1
        )

        assertTrue(result is Result.Success)
        val resultList = (result as Result.Success).data
        assertEquals(2, resultList.size)
        assertEquals(1, resultList.count { it == mockElement1 })
    }

    @Test
    fun `verify list copy is created and original not modified`() {
        val originalList = listOf(mockElement1, mockElement2)

        val result = deleteElement.invoke(
            list = originalList,
            element = mockElement1
        )

        assertNotSame(originalList, (result as Result.Success).data)
    }
}