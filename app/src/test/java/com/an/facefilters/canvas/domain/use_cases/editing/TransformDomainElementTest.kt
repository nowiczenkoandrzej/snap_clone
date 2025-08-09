package com.an.facefilters.canvas.domain.use_cases.editing

import androidx.compose.ui.geometry.Offset
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class TransformDomainElementTest {

    private lateinit var transformElement: TransformElement
    private lateinit var mockElement: Element

    @Before
    fun setUp() {
        transformElement = TransformElement()
        mockElement = mock(Element::class.java)

    }

    @Test
    fun `transform null element returns failure`() {
        val result = transformElement.invoke(
            element = null,
            scale = 1f,
            rotation = 0f,
            offset = Offset.Zero
        )

        assertTrue(result is Result.Failure)
        assertEquals("Element not Found", (result as Result.Failure).message)
    }

    @Test
    fun `transform with valid parameters returns success`() {
        val testScale = 1.5f
        val testRotation = 45f
        val testOffset = Offset(10f, 20f)

        val result = transformElement.invoke(
            element = mockElement,
            scale = testScale,
            rotation = testRotation,
            offset = testOffset
        )

        assertTrue(result is Result.Success)

        verify(mockElement).transform(
            scale = testScale,
            rotation = testRotation,
            offset = testOffset
        )
    }

    @Test
    fun `transform with zero scale handles correctly`() {
        val result = transformElement.invoke(
            element = mockElement,
            scale = 0f,
            rotation = 0f,
            offset = Offset.Zero
        )

        assertTrue(result is Result.Success)
        verify(mockElement).transform(
            scale = 0f,
            rotation = 0f,
            offset = Offset.Zero
        )
    }

    @Test
    fun `transform with negative scale handles correctly`() {
        val negativeScale = -1.5f

        val result = transformElement.invoke(
            element = mockElement,
            scale = negativeScale,
            rotation = 0f,
            offset = Offset.Zero
        )

        assertTrue(result is Result.Success)
        verify(mockElement).transform(
            scale = negativeScale,
            rotation = 0f,
            offset = Offset.Zero
        )
    }

    @Test
    fun `transform with full rotation (360 degrees) handles correctly`() {
        val fullRotation = 360f

        val result = transformElement.invoke(
            element = mockElement,
            scale = 1f,
            rotation = fullRotation,
            offset = Offset.Zero
        )

        assertTrue(result is Result.Success)
        verify(mockElement).transform(
            scale = 1f,
            rotation = fullRotation,
            offset = Offset.Zero
        )
    }

    @Test
    fun `transform with large offset handles correctly`() {
        val largeOffset = Offset(Float.MAX_VALUE, Float.MAX_VALUE)

        val result = transformElement.invoke(
            element = mockElement,
            scale = 1f,
            rotation = 0f,
            offset = largeOffset
        )

        assertTrue(result is Result.Success)
        verify(mockElement).transform(
            scale = 1f,
            rotation = 0f,
            offset = largeOffset
        )
    }

    @Test
    fun `verify new instance is returned`() {
        val result = transformElement.invoke(
            element = mockElement,
            scale = 1f,
            rotation = 0f,
            offset = Offset.Zero
        )

        assertNotSame(mockElement, (result as Result.Success).data)
    }
}