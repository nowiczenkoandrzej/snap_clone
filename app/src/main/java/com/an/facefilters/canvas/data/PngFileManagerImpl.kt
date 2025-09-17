package com.an.facefilters.canvas.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.toArgb
import com.an.facefilters.canvas.domain.managers.PngFileManager
import com.an.facefilters.canvas.domain.model.Element
import com.an.facefilters.canvas.domain.model.Img
import com.an.facefilters.canvas.domain.model.TextModel
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import kotlin.math.abs
import kotlin.math.roundToInt

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

    override suspend fun saveElementsAsBitmap(
        width: Int,
        height: Int,
        elements: List<Element>,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawElementsToCanvas(canvas, elements)

        return bitmap
    }

    override fun saveBitmapToGalleryAsFile(
        bitmap: Bitmap,
    ): File? {

        val fileName = "export_${System.currentTimeMillis()}"

        val relativePath = Environment.DIRECTORY_PICTURES + "/MyCanvasApp"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$fileName.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, relativePath)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        if (uri != null) {
            try {
                contentResolver.openOutputStream(uri).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out!!)
                }

                contentValues.clear()
                contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                contentResolver.update(uri, contentValues, null, null)

                return File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/MyCanvasApp/$fileName.png")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return null

    }


}

fun drawElementsToCanvas(
    canvas: Canvas,
    elements: List<Element>,
) {
    elements.forEachIndexed { index, element ->

        val saveCount = canvas.save()

        val pivot = element.pivot()
        val rotationAngle = if (abs(element.rotationAngle % 90) < 3 || element.rotationAngle % 90 > 87) {
            val rotations = (element.rotationAngle / 90).roundToInt()
            (rotations * 90).toFloat()
        } else element.rotationAngle

        canvas.translate(pivot.x, pivot.y)
        canvas.rotate(rotationAngle)
        canvas.scale(element.scale, element.scale)
        canvas.translate(-pivot.x, -pivot.y)

        when (element) {
            is Img -> {
                val paint = Paint().apply {
                    alpha = (element.alpha * 255).toInt()
                    isFilterBitmap = true
                }
                canvas.drawBitmap(element.bitmap.copy(Bitmap.Config.ARGB_8888, true), element.p1.x, element.p1.y, paint)
            }

            is TextModel -> {
                val paint = Paint().apply {
                    color = element.textStyle.color.toArgb()
                    textSize = element.textStyle.fontSize.value
                    isAntiAlias = true

                }

                val lines = element.text.split("\n")
                var yOffset = 0f
                for (line in lines) {
                    canvas.drawText(line, element.p1.x, element.p1.y + yOffset, paint)
                    yOffset += paint.fontSpacing
                }
            }
        }

        canvas.restoreToCount(saveCount)
    }
}


fun File.toBitmap(): Bitmap{
    return BitmapFactory.decodeFile(this.path)
}
