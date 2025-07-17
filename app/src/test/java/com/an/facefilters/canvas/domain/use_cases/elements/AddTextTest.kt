package com.an.facefilters.canvas.domain.use_cases.elements

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.ui.geometry.Offset
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.TextModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verifyNoInteractions

class AddTextTest {

    private lateinit var addText: AddText
    private lateinit var mockFontFamily: FontFamily
    private lateinit var mockElement: Element
    private lateinit var mockTextModel: TextModel

    @Before
    fun setUp() {
        addText = AddText()
        mockFontFamily = mock(FontFamily::class.java)
        mockElement = mock(Element::class.java)
        mockTextModel = mock(TextModel::class.java)
    }

    @Test
    fun `add text to empty list creates single element`() {
        val testText = "Hello World"
        val testColor = Color.Red
        val emptyList = emptyList<Element>()

        val result = addText.invoke(
            text = testText,
            fontFamily = mockFontFamily,
            elements = emptyList,
            color = testColor
        )

        assertEquals(1, result.size)
        val addedText = result.first() as TextModel
        assertEquals(testText, addedText.text)
        assertEquals(mockFontFamily, addedText.textStyle.fontFamily)
        assertEquals(testColor, addedText.textStyle.color)
        assertEquals(60.sp, addedText.textStyle.fontSize)
        assertEquals(Offset.Zero, addedText.p1)
    }

    @Test
    fun `add text to non-empty list appends element`() {
        val existingList = listOf(mockElement)
        val testText = "Test"

        val result = addText.invoke(
            text = testText,
            fontFamily = mockFontFamily,
            elements = existingList,
            color = Color.Black
        )

        assertEquals(2, result.size)
        assertTrue(result.contains(mockElement))
        assertTrue(result.any { it is TextModel })
    }

    @Test
    fun `verify text style properties are set correctly`() {
        val testText = "Sample"
        val testColor = Color.Blue
        val customFontSize = 60.sp

        val result = addText.invoke(
            text = testText,
            fontFamily = mockFontFamily,
            elements = emptyList(),
            color = testColor
        )

        val textModel = result.first() as TextModel
        assertEquals(testText, textModel.text)
        assertEquals(mockFontFamily, textModel.textStyle.fontFamily)
        assertEquals(testColor, textModel.textStyle.color)
        assertEquals(customFontSize, textModel.textStyle.fontSize)
    }

    @Test
    fun `empty string creates valid text element`() {
        val result = addText.invoke(
            text = "",
            fontFamily = mockFontFamily,
            elements = emptyList(),
            color = Color.Black
        )

        assertEquals(1, result.size)
        assertEquals("", (result.first() as TextModel).text)
    }

    @Test
    fun `original list remains unmodified`() {
        val originalList = listOf(mockElement)

        addText.invoke(
            text = "Test",
            fontFamily = mockFontFamily,
            elements = originalList,
            color = Color.Green
        )

        assertEquals(1, originalList.size)
        verifyNoInteractions(mockElement)
    }

    @Test
    fun `text position starts at Zero offset`() {
        val result = addText.invoke(
            text = "Position Test",
            fontFamily = mockFontFamily,
            elements = emptyList(),
            color = Color.Magenta
        )

        assertEquals(Offset.Zero, (result.first() as TextModel).p1)
    }
}