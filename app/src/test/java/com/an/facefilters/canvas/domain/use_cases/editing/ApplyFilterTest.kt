package com.an.facefilters.canvas.domain.use_cases.editing

import android.graphics.Bitmap
import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class ApplyFilterTest {
    private lateinit var applyFilter: ApplyFilter
    private lateinit var mockBitmap: Bitmap
    private lateinit var mockOriginalBitmap: Bitmap
    private lateinit var mockFilter: PhotoFilter
    private lateinit var testImg: Img

    @Before
    fun setUp() {
        mockBitmap = mock(Bitmap::class.java)
        mockOriginalBitmap = mock(Bitmap::class.java)
        testImg = Img(
            bitmap = mockBitmap,
            originalBitmap = mockOriginalBitmap
        )
        mockFilter = PhotoFilter(
            name = "Sepia",
            apply = { bitmap -> mockBitmap }
        )
        applyFilter = ApplyFilter()
    }

    @Test
    fun `apply filter to null element, returns failure`() {
        val result = applyFilter.invoke(element = null, filter = mockFilter)
        assertEquals("Element not Fount", (result as Result.Failure).message)
    }

    @Test
    fun `apply filter to non-image element, returns failure`() {
        val nonImgElement = mock(Element::class.java)
        val result = applyFilter.invoke(element = nonImgElement, filter = mockFilter)
        assertEquals("Pick Image", (result as Result.Failure).message)
    }

    @Test
    fun `apply filter to image, returns success with filtered bitmap`() {
        val result = applyFilter.invoke(element = testImg, filter = mockFilter)

        assertTrue(result is Result.Success)
        val filteredImg = (result as Result.Success).data

        assertEquals(mockBitmap, filteredImg.bitmap)
        assertEquals("Sepia", filteredImg.currentFilter)
        assertEquals(mockOriginalBitmap, filteredImg.originalBitmap)
    }

}