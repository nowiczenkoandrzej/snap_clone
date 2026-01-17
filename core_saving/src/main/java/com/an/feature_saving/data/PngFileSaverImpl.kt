package com.an.feature_saving.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.res.ResourcesCompat
import com.an.core_editor.domain.ImageRenderer
import com.an.core_editor.presentation.model.UiElement
import com.an.core_editor.presentation.model.UiImageModel

import com.an.core_editor.presentation.model.UiStickerModel
import com.an.core_editor.presentation.model.UiTextModel
import com.an.feature_saving.domain.PngFileSaver
import kotlin.math.abs
import kotlin.math.roundToInt

class PngFileSaverImpl(
    private val renderer: ImageRenderer,
    private val context: Context
): PngFileSaver {
    override suspend fun saveImage(
        elements: List<UiElement>,
        canvasWidth: Int,
        canvasHeight: Int,
    ) {

        val collage = renderFullCollage(
            elements = elements,
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "collage_${System.currentTimeMillis()}.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyEditor")
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { out ->
                collage.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            resolver.update(it, contentValues, null, null)
        }


    }

    private fun renderFullCollage(
        elements: List<UiElement>,
        canvasWidth: Int,
        canvasHeight: Int,
    ): Bitmap {
        val output = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        elements.forEach { element ->

            val saveCount = canvas.save()

            val center = element.center()
            val rotationAngle = if (abs(element.rotationAngle % 90) < 3 || element.rotationAngle % 90 > 87) {
                val rotations = (element.rotationAngle / 90).roundToInt()
                (rotations * 90).toFloat()
            } else element.rotationAngle

            canvas.translate(center.x, center.y)
            canvas.rotate(rotationAngle)
            canvas.scale(element.scale, element.scale)
            canvas.translate(-center.x, -center.y)


            when (element) {
                is UiImageModel -> {

                    if(element.bitmap != null) {
                        val paint = Paint().apply {
                            alpha = (element.alpha * 255).toInt()
                            isFilterBitmap = true

                        }
                        canvas.drawBitmap(
                            element.bitmap!!.copy(Bitmap.Config.ARGB_8888, true),
                            element.position.x,
                            element.position.y,
                            paint
                        )
                    }


                }

                is UiTextModel -> {
                    val paint = Paint().apply {
                        color = element.fontColor.toArgb()
                        textSize = element.fontSize
                        typeface = ResourcesCompat.getFont(context, element.fontItem.fontResId)
                        isAntiAlias = true
                        isAntiAlias = true

                    }
                    val fm = paint.fontMetrics
                    val baselineOffset = -fm.ascent

                    val lines = element.text.split("\n")
                    var yOffset = 0f
                    for (line in lines) {
                        val y = element.position.y + baselineOffset + yOffset
                        canvas.drawText(line, element.position.x, y, paint)
                        yOffset += paint.fontSpacing
                    }

                }

                is UiStickerModel -> {
                    val inputStream = context.assets.open(element.stickerPath)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    val paint = Paint().apply {
                        alpha = (element.alpha * 255).toInt()
                        isFilterBitmap = true
                    }
                    canvas.rotate(element.rotationAngle, element.center().x, element.center().y)
                    canvas.scale(element.scale, element.scale, element.center().x, element.center().y)
                    canvas.drawBitmap(bitmap, element.position.x, element.position.y, paint)
                }
            }
            canvas.restoreToCount(saveCount)
        }



        return output
    }

}