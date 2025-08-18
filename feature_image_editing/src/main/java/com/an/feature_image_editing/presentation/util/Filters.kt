package com.an.feature_image_editing.presentation.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import kotlin.math.min
import kotlin.math.sqrt

data class PhotoFilter(
    val name: String,
    val apply: (Bitmap) -> Bitmap
)

fun getPhotoFilterByName(name: String): PhotoFilter {
    val filters = getFiltersList()

    filters.forEach { filter ->
        if(filter.name == name) return filter
    }
    return filters[0]
}


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
        PhotoFilter("Warm") { bitmap -> bitmap.applyWarmFilter() },
        PhotoFilter("Invert") { bitmap -> bitmap.applyInvertFilter() },
        PhotoFilter("Blue Tint") { bitmap -> bitmap.applyBlueTintFilter() },
        PhotoFilter("Red Tint") { bitmap -> bitmap.applyRedTintFilter() },
        PhotoFilter("Toaster") { it.applyToasterFilter() },
        PhotoFilter("Purple Tint") { it.applyPurpleTintFilter() },
        PhotoFilter("Pastel") { it.applyPastelFilter() },
        PhotoFilter("B&W High Contrast") { it.applyHardBlackWhiteFilter() },
        PhotoFilter("Pixel Art") { it.applyPixelArtFilter(16) },
        PhotoFilter("Sobel Edge") { it.applySobelFilter() }
    )
}
fun Bitmap.applySobelFilter(): Bitmap {
    val source = this.applyGrayscaleFilter() // działa lepiej na grayscale
    val width = source.width
    val height = source.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val pixels = IntArray(width * height)
    source.getPixels(pixels, 0, width, 0, 0, width, height)

    val outputPixels = IntArray(width * height)

    // Macierze Sobela
    val gx = arrayOf(
        intArrayOf(-1, 0, 1),
        intArrayOf(-2, 0, 2),
        intArrayOf(-1, 0, 1)
    )
    val gy = arrayOf(
        intArrayOf(-1, -2, -1),
        intArrayOf(0, 0, 0),
        intArrayOf(1, 2, 1)
    )

    for (y in 1 until height - 1) {
        for (x in 1 until width - 1) {
            var sumX = 0
            var sumY = 0

            for (ky in -1..1) {
                for (kx in -1..1) {
                    val pixel = pixels[(x + kx) + (y + ky) * width]
                    val gray = (pixel shr 16) and 0xFF // red channel (grayscale)

                    sumX += gray * gx[ky + 1][kx + 1]
                    sumY += gray * gy[ky + 1][kx + 1]
                }
            }

            val magnitude = min(255, sqrt((sumX * sumX + sumY * sumY).toDouble()).toInt())
            val edgeColor = 0xFF shl 24 or (magnitude shl 16) or (magnitude shl 8) or magnitude
            outputPixels[x + y * width] = edgeColor
        }
    }

    result.setPixels(outputPixels, 0, width, 0, 0, width, height)
    return result
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
fun Bitmap.applyInvertFilter(): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f,  0f, 1f, 0f
        )
    )
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}
fun Bitmap.applyBlueTintFilter(): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            0.8f, 0f, 0f, 0f, 0f,
            0f, 0.8f, 0f, 0f, 0f,
            0f, 0f, 1.5f, 0f, 30f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}
fun Bitmap.applyRedTintFilter(): Bitmap {
    val sourceBitmap = convertToSoftwareBitmap(this)
    val width = sourceBitmap.width
    val height = sourceBitmap.height
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            1.5f, 0f, 0f, 0f, 30f,
            0f, 0.8f, 0f, 0f, 0f,
            0f, 0f, 0.8f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(sourceBitmap, 0f, 0f, paint)

    return result
}
fun Bitmap.applyToasterFilter(): Bitmap {
    val source = convertToSoftwareBitmap(this)
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val colorMatrix = ColorMatrix(
        floatArrayOf(
            1.2f, 0.05f, 0.05f, 0f, -10f,
            0.05f, 1.1f, 0.05f, 0f, -10f,
            0.05f, 0.05f, 1.1f, 0f, -10f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
    canvas.drawBitmap(source, 0f, 0f, paint)

    return result
}
fun Bitmap.applyPurpleTintFilter(): Bitmap {
    val source = convertToSoftwareBitmap(this)
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val matrix = ColorMatrix(
        floatArrayOf(
            1.1f, 0.2f, 0.4f, 0f, 20f,
            0.1f, 1f, 0.1f, 0f, 10f,
            0.3f, 0.2f, 1.2f, 0f, 30f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    paint.colorFilter = ColorMatrixColorFilter(matrix)
    canvas.drawBitmap(source, 0f, 0f, paint)

    return result
}
fun Bitmap.applyPastelFilter(): Bitmap {
    val source = convertToSoftwareBitmap(this)
    val result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    val canvas = Canvas(result)
    val paint = Paint()
    val matrix = ColorMatrix(
        floatArrayOf(
            0.9f, 0.1f, 0.1f, 0f, 20f,
            0.1f, 0.9f, 0.1f, 0f, 20f,
            0.1f, 0.1f, 0.9f, 0f, 20f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    paint.colorFilter = ColorMatrixColorFilter(matrix)
    canvas.drawBitmap(source, 0f, 0f, paint)

    return result
}
fun Bitmap.applyHardBlackWhiteFilter(): Bitmap {
    val grayscale = this.applyGrayscaleFilter()
    val pixels = IntArray(grayscale.width * grayscale.height)
    grayscale.getPixels(pixels, 0, grayscale.width, 0, 0, grayscale.width, grayscale.height)

    for (i in pixels.indices) {
        val color = pixels[i]
        val gray = (color shr 16 and 0xFF) // red channel as grayscale base
        pixels[i] = if (gray < 128) 0xFF000000.toInt() else 0xFFFFFFFF.toInt()
    }

    val result = Bitmap.createBitmap(grayscale.width, grayscale.height, Bitmap.Config.ARGB_8888)
    result.setPixels(pixels, 0, grayscale.width, 0, 0, grayscale.width, grayscale.height)
    return result
}
fun Bitmap.applyPixelArtFilter(pixelSize: Int = 16): Bitmap {
    val source = convertToSoftwareBitmap(this)

    // Zmniejsz obraz
    val scaledDown = Bitmap.createScaledBitmap(
        source,
        source.width / pixelSize,
        source.height / pixelSize,
        false // bez interpolacji
    )

    // Powiększ z powrotem do oryginalnych rozmiarów
    return Bitmap.createScaledBitmap(
        scaledDown,
        source.width,
        source.height,
        false // bez wygładzania
    )
}

fun convertToSoftwareBitmap(bitmap: Bitmap): Bitmap {
    return if (bitmap.config == Bitmap.Config.HARDWARE) {
        bitmap.copy(Bitmap.Config.ARGB_8888, false)
    } else {
        bitmap
    }
}