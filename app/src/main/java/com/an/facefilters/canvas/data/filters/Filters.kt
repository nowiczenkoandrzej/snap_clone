package com.an.facefilters.canvas.data.filters

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint

data class PhotoFilter(
    val name: String,
    val apply: (Bitmap) -> Bitmap
)

// Lista dostępnych filtrów
fun getFiltersList(): List<PhotoFilter> {
    return listOf(
        PhotoFilter("Original") { bitmap -> bitmap },
        PhotoFilter("Grayscale") { bitmap -> bitmap.applyGrayscaleFilter() },
        PhotoFilter("Sepia") { bitmap -> bitmap.applySepiaFilter() },
        PhotoFilter("Brightness") { bitmap -> bitmap.applyBrightnessFilter(1.3f) },
        PhotoFilter("Contrast") { bitmap -> bitmap.applyContrastFilter(1.5f) },
        PhotoFilter("Vintage") { bitmap -> bitmap.applyVintageFilter() },
        PhotoFilter("Cool") { bitmap -> bitmap.applyCoolFilter() },
        PhotoFilter("Warm") { bitmap -> bitmap.applyWarmFilter() }
    )
}


fun Bitmap.applyGrayscaleFilter(): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}

fun Bitmap.applySepiaFilter(): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix()

    val sepiaMatrix = floatArrayOf(
        0.393f, 0.769f, 0.189f, 0f, 0f,
        0.349f, 0.686f, 0.168f, 0f, 0f,
        0.272f, 0.534f, 0.131f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )

    colorMatrix.set(sepiaMatrix)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}

fun Bitmap.applyBrightnessFilter(brightness: Float): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix()

    val brightnessMatrix = floatArrayOf(
        brightness, 0f, 0f, 0f, 0f,
        0f, brightness, 0f, 0f, 0f,
        0f, 0f, brightness, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )

    colorMatrix.set(brightnessMatrix)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}

fun Bitmap.applyContrastFilter(contrast: Float): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix()

    val offset = (1f - contrast) * 0.5f * 255f

    val contrastMatrix = floatArrayOf(
        contrast, 0f, 0f, 0f, offset,
        0f, contrast, 0f, 0f, offset,
        0f, 0f, contrast, 0f, offset,
        0f, 0f, 0f, 1f, 0f
    )

    colorMatrix.set(contrastMatrix)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}

fun Bitmap.applyVintageFilter(): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix()

    val vintageMatrix = floatArrayOf(
        0.9f, 0.5f, 0.1f, 0f, 0f,
        0.3f, 0.8f, 0.1f, 0f, 0f,
        0.2f, 0.3f, 0.5f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )

    colorMatrix.set(vintageMatrix)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}

fun Bitmap.applyCoolFilter(): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix()

    val coolMatrix = floatArrayOf(
        0.6f, 0f, 0f, 0f, 0f,
        0f, 0.7f, 0f, 0f, 0f,
        0f, 0f, 1.8f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )

    colorMatrix.set(coolMatrix)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}

fun Bitmap.applyWarmFilter(): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix()

    val warmMatrix = floatArrayOf(
        1.2f, 0f, 0f, 0f, 0f,
        0f, 1.1f, 0f, 0f, 0f,
        0f, 0f, 0.8f, 0f, 0f,
        0f, 0f, 0f, 1f, 0f
    )

    colorMatrix.set(warmMatrix)
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}

fun convertToSoftwareBitmap(bitmap: Bitmap): Bitmap {
    return if (bitmap.config == Bitmap.Config.HARDWARE) {
        bitmap.copy(Bitmap.Config.ARGB_8888, false)
    } else {
        bitmap
    }
}