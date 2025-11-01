package com.an.feature_canvas.data

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.os.Environment
import android.provider.MediaStore
import com.an.core_editor.domain.ImageRenderer
import com.an.core_editor.domain.model.DomainElement
import com.an.core_editor.domain.model.DomainImageModel
import com.an.core_editor.domain.model.DomainStickerModel
import com.an.core_editor.domain.model.DomainTextModel
import com.an.feature_canvas.domain.PngFileSaver

class PngFileSaverImpl(
    private val renderer: ImageRenderer,
    private val context: Context
): PngFileSaver {
    override suspend fun saveImage(
        elements: List<DomainElement>,
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
        elements: List<DomainElement>,
        canvasWidth: Int,
        canvasHeight: Int,
    ): Bitmap {
        val output = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        elements.forEach {
            elements.forEach { element ->
                when (element) {
                    is DomainImageModel -> {
                        renderer.render(element)?.let { bmp ->
                            val matrix = Matrix().apply {
                                postTranslate(-bmp.width / 2f, -bmp.height / 2f)
                                postRotate(element.rotationAngle)
                                postScale(element.scale, element.scale)
                                postTranslate(element.position.x, element.position.y)
                            }
                            paint.alpha = (element.alpha * 255).toInt()
                            canvas.drawBitmap(bmp, matrix, paint)
                        }
                    }

                    is DomainTextModel -> {

                    }

                    is DomainStickerModel -> {
                        // np. drawSticker(canvas, element)
                    }
                }
            }


        }
        return output
    }

}