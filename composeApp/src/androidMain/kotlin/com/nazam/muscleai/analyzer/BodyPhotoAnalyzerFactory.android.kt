package com.nazam.muscleai.domain.analyzer

import android.graphics.Bitmap
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.min

actual class BodyPhotoAnalyzerFactory {
    actual fun create(): BodyPhotoAnalyzer = AndroidBodyPhotoAnalyzer()
}

private class AndroidBodyPhotoAnalyzer : BodyPhotoAnalyzer {

    override suspend fun analyze(photo: PhotoInput): PhotoAnalysisResult {
        delay(400)

        val bitmap = photo.bitmap

        if (bitmap.width < 200 || bitmap.height < 200) {
            return PhotoAnalysisResult(
                isValid = false,
                message = "Photo trop petite."
            )
        }

        val brightness = averageBrightness(bitmap)

        if (brightness < 40) {
            return PhotoAnalysisResult(false, "Photo trop sombre.")
        }

        if (brightness > 220) {
            return PhotoAnalysisResult(false, "Photo trop lumineuse.")
        }

        val sharpness = sharpnessScore(bitmap)

        if (sharpness < 15) {
            return PhotoAnalysisResult(false, "Photo floue. Reste immobile.")
        }

        return PhotoAnalysisResult(
            isValid = true,
            message = "Photo OK. Analyse prête."
        )
    }

    private fun averageBrightness(bitmap: Bitmap): Int {
        var total = 0L
        var count = 0

        val step = 20
        for (y in 0 until bitmap.height step step) {
            for (x in 0 until bitmap.width step step) {
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

    private fun sharpnessScore(bitmap: Bitmap): Int {
        var totalDiff = 0L
        var count = 0

        val step = 20
        for (y in 0 until bitmap.height - step step step) {
            for (x in 0 until bitmap.width - step step step) {

                val p1 = bitmap.getPixel(x, y)
                val p2 = bitmap.getPixel(x + step, y + step)

                val gray1 = ((p1 shr 16) and 0xff +
                        (p1 shr 8) and 0xff +
                        (p1 and 0xff)) / 3

                val gray2 = ((p2 shr 16) and 0xff +
                        (p2 shr 8) and 0xff +
                        (p2 and 0xff)) / 3

                totalDiff += abs(gray1 - gray2)
                count++
            }
        }

        return (totalDiff / count).toInt()
    }
}
