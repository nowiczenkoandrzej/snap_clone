package com.an.facefilters.canvas.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.IntSize
import com.an.facefilters.canvas.domain.managers.PngFileManager
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.TextModel
import java.io.File
import java.util.UUID

class PngFileManagerImpl(
    private val context: Context
): PngFileManager {

    companion object {
        const val USER_STICKER = "user_sticker"
    }

    override suspend fun saveSticker(bitmap: Bitmap) {
        val dir = File(context.filesDir, USER_STICKER)
        if(!dir.exists()) dir.mkdirs()

        val filename = "${UUID.randomUUID()}.png"
        val file = File(dir, filename)

        file.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }


    }

    override suspend fun loadUserStickers(): List<File> {
        val dir = File(context.filesDir, USER_STICKER)
        return dir.listFiles()?.filter { it.extension == "png" } ?: emptyList()
    }

    override suspend fun saveAsPng(
        elements: List<Element>,
        textMeasurer: TextMeasurer,
    ): Uri? {
        val canvasSize = calculateCanvasSize(elements)
        if (canvasSize.width <= 0 || canvasSize.height <= 0) return null

        val minX = elements.minOfOrNull {
            when (it) {
                is Img -> it.p1.x
                is TextModel -> it.p1.x
                else -> 0f
            }
        } ?: 0f
        val minY = elements.minOfOrNull {
            when (it) {
                is Img -> it.p1.y
                is TextModel -> it.p1.y
                else -> 0f
            }
        } ?: 0f

        val bitmap = Bitmap.createBitmap(
            canvasSize.width,
            canvasSize.height,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.TRANSPARENT) // Możesz zmienić tło

        elements.forEach { element ->
            val offsetX = element.p1.x - minX + 20f // Dodaj margines
            val offsetY = element.p1.y - minY + 20f // Dodaj margines

            when (element) {
                is Img -> {
                    val matrix = Matrix().apply {
                        postTranslate(offsetX, offsetY)
                        postScale(element.scale, element.scale, offsetX, offsetY)
                        postRotate(
                            element.rotationAngle,
                            offsetX + element.bitmap.width * element.scale / 2,
                            offsetY + element.bitmap.height * element.scale / 2
                        )
                    }

                    val paint = Paint().apply {
                        alpha = (element.alpha * 255).toInt()
                    }

                    canvas.drawBitmap(element.bitmap, matrix, paint)
                }
                is TextModel -> {
                    val textPaint = element.textStyle.toPaint().apply {
                        alpha = (element.alpha * 255).toInt()
                    }

                    val matrix = Matrix().apply {
                        postTranslate(offsetX, offsetY)
                        postScale(element.scale, element.scale, offsetX, offsetY)
                        postRotate(
                            element.rotationAngle,
                            offsetX + element.text.length * element.textStyle.fontSize.value * 0.6f * element.scale / 2,
                            offsetY + element.textStyle.fontSize.value * element.scale / 2
                        )
                    }

                    canvas.save()
                    canvas.concat(matrix)
                    canvas.drawText(element.text, 0f, element.textStyle.fontSize.value, textPaint)
                    canvas.restore()
                }
            }
        }
        return try {
            val filename = "canvas_${System.currentTimeMillis()}.png"
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
            }

            val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            uri?.let {
                resolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
            }
            uri
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            bitmap.recycle()
        }
    }


    fun calculateCanvasSize(elements: List<Element>): IntSize {
        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        elements.forEach { element ->
            when (element) {
                is Img -> {
                    val width = element.bitmap.width * element.scale
                    val height = element.bitmap.height * element.scale
                    minX = minOf(minX, element.p1.x)
                    minY = minOf(minY, element.p1.y)
                    maxX = maxOf(maxX, element.p1.x + width)
                    maxY = maxOf(maxY, element.p1.y + height)
                }
                is TextModel -> {
                    val height = element.textStyle.fontSize.value * element.scale
                    val width = element.text.length * height * 0.6f
                    minX = minOf(minX, element.p1.x)
                    minY = minOf(minY, element.p1.y)
                    maxX = maxOf(maxX, element.p1.x + width)
                    maxY = maxOf(maxY, element.p1.y + height)
                }
                // Dodaj obsługę innych typów elementów jeśli są
            }
        }

        // Dodaj margines
        val margin = 20f
        return IntSize(
            width = (maxX - minX + margin * 2).toInt(),
            height = (maxY - minY + margin * 2).toInt()
        )
    }

}

fun File.toBitmap(): Bitmap{
    return BitmapFactory.decodeFile(this.path)
}

fun TextStyle.toPaint(): Paint {
    return Paint().apply {
        color = this@toPaint.color.toArgb()
        textSize = this@toPaint.fontSize.value
        typeface = this@toPaint.fontFamily?.toTypeface() ?: Typeface.DEFAULT
        isAntiAlias = true
    }
}

fun FontFamily?.toTypeface(): Typeface? {
    return when (this) {
        FontFamily.Default -> Typeface.DEFAULT
        FontFamily.Cursive -> Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
        FontFamily.Monospace -> Typeface.MONOSPACE
        FontFamily.SansSerif -> Typeface.SANS_SERIF
        FontFamily.Serif -> Typeface.SERIF
        else -> null
    }
}