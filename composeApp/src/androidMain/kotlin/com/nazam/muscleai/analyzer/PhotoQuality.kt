package com.nazam.muscleai.analyzer

import android.graphics.Bitmap
import kotlin.math.abs

object PhotoQuality {

    fun isTooSmall(bitmap: Bitmap): Boolean =
        bitmap.width < 200 || bitmap.height < 200

    fun isTooDark(bitmap: Bitmap): Boolean =
        averageBrightness(bitmap) < 40

    fun isTooBright(bitmap: Bitmap): Boolean =
        averageBrightness(bitmap) > 220

    fun isTooBlurry(bitmap: Bitmap): Boolean =
        sharpnessScore(bitmap) < 15

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

                val g1 = (((p1 shr 16) and 0xff) + ((p1 shr 8) and 0xff) + (p1 and 0xff)) / 3
                val g2 = (((p2 shr 16) and 0xff) + ((p2 shr 8) and 0xff) + (p2 and 0xff)) / 3

                totalDiff += abs(g1 - g2)
                count++
            }
        }
        return (totalDiff / count).toInt()
    }
}
