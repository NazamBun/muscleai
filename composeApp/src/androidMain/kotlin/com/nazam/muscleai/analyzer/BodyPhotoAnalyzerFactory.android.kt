package com.nazam.muscleai.domain.analyzer

import android.graphics.Bitmap
import kotlinx.coroutines.delay
import kotlin.math.min

actual class BodyPhotoAnalyzerFactory {
    actual fun create(): BodyPhotoAnalyzer = AndroidBodyPhotoAnalyzer()
}

private class AndroidBodyPhotoAnalyzer : BodyPhotoAnalyzer {

    override suspend fun analyze(photo: PhotoInput): PhotoAnalysisResult {
        delay(400)

        val bitmap = photo.bitmap

        if (bitmap.width < 300 || bitmap.height < 300) {
            return PhotoAnalysisResult(
                isValid = false,
                message = "Photo trop petite. Rapproche-toi un peu."
            )
        }

        val brightness = calculateBrightness(bitmap)

        if (brightness < 60) {
            return PhotoAnalysisResult(
                isValid = false,
                message = "Photo trop sombre. Mets-toi dans un endroit plus lumineux."
            )
        }

        return PhotoAnalysisResult(
            isValid = true,
            message = "Photo correcte. Analyse validée."
        )
    }

    private fun calculateBrightness(bitmap: Bitmap): Int {
        val width = bitmap.width
        val height = bitmap.height
        var total = 0L
        var count = 0

        val step = min(width, height) / 50 + 1

        for (x in 0 until width step step) {
            for (y in 0 until height step step) {
                val pixel = bitmap.getPixel(x, y)
                val r = (pixel shr 16) and 0xff
                val g = (pixel shr 8) and 0xff
                val b = pixel and 0xff
                total += (r + g + b) / 3
                count++
            }
        }

        return (total / count).toInt()
    }
}
