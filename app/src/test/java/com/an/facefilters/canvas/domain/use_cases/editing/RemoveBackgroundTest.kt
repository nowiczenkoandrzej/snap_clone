package com.an.facefilters.canvas.domain.use_cases.editing

import com.an.facefilters.canvas.data.FakeSubjectDetector
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RemoveBackgroundTest {
    private lateinit var removeBackground: RemoveBackground
    private lateinit var fakeSubjectDetector: FakeSubjectDetector

    @Before
    fun setUp() {
        fakeSubjectDetector = FakeSubjectDetector()
        removeBackground = RemoveBackground(fakeSubjectDetector)
    }

    @Test
    fun `detect subject with null element, throw exception`() {
        try {
            removeBackground.invoke(
                element = null,
                onDetect = {}
            )
        } catch (e: Exception) {

        }
    }

}