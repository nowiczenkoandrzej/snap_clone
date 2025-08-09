package com.an.facefilters.canvas.domain.use_cases.editing

import com.an.facefilters.canvas.domain.model.Element
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import com.an.facefilters.canvas.domain.model.Result
import org.mockito.kotlin.verify


class ChangeDomainElementAlphaTest {
    private lateinit var changeElementAlpha: ChangeElementAlpha
    private lateinit var mockElement: Element

    @Before
    fun setUp() {
        changeElementAlpha = ChangeElementAlpha()
        mockElement = mock(Element::class.java)
    }

    @Test
    fun `change alpha with null element returns failure`() {
        val result = changeElementAlpha.invoke(
            element = null,
            alpha = 0.5f
        )

        assertTrue(result is Result.Failure)
        assertEquals("Element not Found", (result as Result.Failure).message)
    }

    @Test
    fun `change alpha with valid element returns success`() {
        val newAlpha = 0.7f
        val expectedElement = mock(Element::class.java)

        `when`(mockElement.setAlpha(newAlpha)).thenReturn(expectedElement)

        val result = changeElementAlpha.invoke(
            element = mockElement,
            alpha = newAlpha
        )

        assertTrue(result is Result.Success)
        assertEquals(expectedElement, (result as Result.Success).data)
        verify(mockElement).setAlpha(newAlpha)
    }

    @Test
    fun `change alpha with minimum value (0f)`() {
        val minAlpha = 0f
        val expectedElement = mock(Element::class.java)

        `when`(mockElement.setAlpha(minAlpha)).thenReturn(expectedElement)

        val result = changeElementAlpha.invoke(
            element = mockElement,
            alpha = minAlpha
        )

        assertTrue(result is Result.Success)
        verify(mockElement).setAlpha(minAlpha)
    }

    @Test
    fun `change alpha with maximum value (1f)`() {
        val maxAlpha = 1f
        val expectedElement = mock(Element::class.java)

        `when`(mockElement.setAlpha(maxAlpha)).thenReturn(expectedElement)

        val result = changeElementAlpha.invoke(
            element = mockElement,
            alpha = maxAlpha
        )

        assertTrue(result is Result.Success)
        verify(mockElement).setAlpha(maxAlpha)
    }

    @Test
    fun `change alpha with out of range value below 0 returns clamped value`() {
        val negativeAlpha = -0.5f
        val expectedElement = mock(Element::class.java)

        `when`(mockElement.setAlpha(0f)).thenReturn(expectedElement)

        val result = changeElementAlpha.invoke(
            element = mockElement,
            alpha = negativeAlpha
        )

        assertTrue(result is Result.Success)
        verify(mockElement).setAlpha(0f)
    }

    @Test
    fun `change alpha with out of range value above 1 returns clamped value`() {
        val excessiveAlpha = 1.5f
        val expectedElement = mock(Element::class.java)

        `when`(mockElement.setAlpha(1f)).thenReturn(expectedElement)

        val result = changeElementAlpha.invoke(
            element = mockElement,
            alpha = excessiveAlpha
        )

        assertTrue(result is Result.Success)
        verify(mockElement).setAlpha(1f)
    }

    @Test
    fun `verify element copy is returned not same instance`() {
        val newAlpha = 0.3f
        val newElement = mock(Element::class.java)

        `when`(mockElement.setAlpha(newAlpha)).thenReturn(newElement)

        val result = changeElementAlpha.invoke(
            element = mockElement,
            alpha = newAlpha
        )

        assertNotSame(mockElement, (result as Result.Success).data)
        assertSame(newElement, result.data)
    }
}