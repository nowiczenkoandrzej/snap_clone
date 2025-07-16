package com.an.facefilters.canvas.domain.use_cases.editing

import android.graphics.Bitmap
import com.an.facefilters.canvas.data.filters.PhotoFilter
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.Result
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class ApplyFilterTest {
    private lateinit var applyFilter: ApplyFilter

    private val fakeBitmap = mock(Bitmap::class.java)
    private val fakeOriginalBitmap = mock(Bitmap::class.java)

    private val fakeImg = Img(
        bitmap = fakeOriginalBitmap,
        originalBitmap = fakeOriginalBitmap
    )

    private val resultImg = Img(
        bitmap = fakeBitmap,
        originalBitmap = fakeOriginalBitmap
    )

    private val failedResult = Result.Failure("Element not Fount")
    private val succeedResult = Result.Success(resultImg)



    private val fakeFilter = PhotoFilter(
        name = "Sepia",
        apply = { bitmap -> fakeBitmap}
    )

    @Before
    fun setUp() {
        applyFilter = ApplyFilter()
    }

    @Test
    fun `apply filter to null element, fail`() {
        val result = applyFilter.invoke(
            element = null,
            filter = fakeFilter
        )

        assertEquals(failedResult, result)
    }

    @Test
    fun `apply filter to not image element, fail`() {
        val result = applyFilter.invoke(
            element = null,
            filter = fakeFilter
        )

        assertEquals(failedResult, result)
    }
    @Test
    fun `apply filter to bitmap, success`() {
        val result = applyFilter.invoke(
            element = fakeImg,
            filter = fakeFilter
        )
        assertTrue(result is Result.Success)

        val filtered = (result as Result.Success).data

        assertEquals(fakeBitmap, filtered.bitmap)
        assertEquals("Sepia", filtered.currentFilter)
        assertEquals(fakeImg.originalBitmap, filtered.originalBitmap)
    }
}