package com.nazam.muscleai.domain.analyzer

import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.nazam.muscleai.analyzer.AndroidAppContext
import com.nazam.muscleai.analyzer.PhotoQuality
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

actual class BodyPhotoAnalyzerFactory {
    actual fun create(): BodyPhotoAnalyzer = AndroidPoseAnalyzer()
}

private class AndroidPoseAnalyzer : BodyPhotoAnalyzer {

    private companion object {
        const val LS = 11
        const val RS = 12
        const val LE = 13
        const val RE = 14
        const val LW = 15
        const val RW = 16
    }

    private val landmarker: PoseLandmarker by lazy { createLandmarker() }

    override suspend fun analyze(photo: PhotoInput): PhotoAnalysisResult {
        delay(150)

        val bitmap = photo.bitmap

        when {
            PhotoQuality.isTooSmall(bitmap) -> return PhotoAnalysisResult(false, "Photo trop petite.", 0)
            PhotoQuality.isTooDark(bitmap) -> return PhotoAnalysisResult(false, "Photo trop sombre.", 0)
            PhotoQuality.isTooBright(bitmap) -> return PhotoAnalysisResult(false, "Photo trop lumineuse.", 0)
            PhotoQuality.isTooBlurry(bitmap) -> return PhotoAnalysisResult(false, "Photo floue. Reste immobile.", 0)
        }

        val mpImage = BitmapImageBuilder(bitmap).build()
        val result = landmarker.detect(mpImage)

        val score = armVisibilityScore(result) // 0..100

        return if (score >= 60) {
            PhotoAnalysisResult(true, "Bras détecté ✅", score)
        } else {
            PhotoAnalysisResult(false, "Je ne vois pas bien le bras. Cadre mieux ton bras.", score)
        }
    }

    private fun armVisibilityScore(result: PoseLandmarkerResult): Int {
        val poses = result.landmarks()
        if (poses.isEmpty()) return 0
        val lm = poses[0]

        fun vis(i: Int): Float {
            val opt = lm[i].visibility()
            return if (opt.isPresent) opt.get() else 0f
        }

        val left = (vis(LS) + vis(LE) + vis(LW)) / 3f
        val right = (vis(RS) + vis(RE) + vis(RW)) / 3f

        val best = maxOf(left, right) // 0..1
        return (best * 100f).roundToInt().coerceIn(0, 100)
    }

    private fun createLandmarker(): PoseLandmarker {
        val context = AndroidAppContext.get()

        val base = BaseOptions.builder()
            .setModelAssetPath("pose_landmarker_lite.task")
            .build()

        val options = PoseLandmarker.PoseLandmarkerOptions.builder()
            .setBaseOptions(base)
            .setRunningMode(RunningMode.IMAGE)
            .build()

        return PoseLandmarker.createFromOptions(context, options)
    }
}
