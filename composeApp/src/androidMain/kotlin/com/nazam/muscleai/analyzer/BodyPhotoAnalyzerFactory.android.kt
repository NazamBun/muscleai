package com.nazam.muscleai.domain.analyzer

import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarker
import com.google.mediapipe.tasks.vision.poselandmarker.PoseLandmarkerResult
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.nazam.muscleai.analyzer.AndroidAppContext
import com.nazam.muscleai.analyzer.PhotoQuality
import kotlinx.coroutines.delay

actual class BodyPhotoAnalyzerFactory {
    actual fun create(): BodyPhotoAnalyzer = AndroidPoseAnalyzer()
}

private class AndroidPoseAnalyzer : BodyPhotoAnalyzer {

    // Indices MediaPipe Pose (constants stables)
    private companion object {
        const val LEFT_SHOULDER = 11
        const val RIGHT_SHOULDER = 12
        const val LEFT_ELBOW = 13
        const val RIGHT_ELBOW = 14
        const val LEFT_WRIST = 15
        const val RIGHT_WRIST = 16
    }

    private val landmarker: PoseLandmarker by lazy { createLandmarker() }

    override suspend fun analyze(photo: PhotoInput): PhotoAnalysisResult {
        delay(150)

        val bitmap = photo.bitmap

        // 1) Qualité photo (rapide)
        when {
            PhotoQuality.isTooSmall(bitmap) -> return PhotoAnalysisResult(false, "Photo trop petite.")
            PhotoQuality.isTooDark(bitmap) -> return PhotoAnalysisResult(false, "Photo trop sombre.")
            PhotoQuality.isTooBright(bitmap) -> return PhotoAnalysisResult(false, "Photo trop lumineuse.")
            PhotoQuality.isTooBlurry(bitmap) -> return PhotoAnalysisResult(false, "Photo floue. Reste immobile.")
        }

        // 2) IA Pose (MediaPipe)
        val mpImage = BitmapImageBuilder(bitmap).build()
        val result = landmarker.detect(mpImage)

        val armOk = hasArmLandmarks(result)

        return if (armOk) {
            PhotoAnalysisResult(true, "Bras détecté ✅")
        } else {
            PhotoAnalysisResult(false, "Je ne vois pas bien le bras. Cadre mieux ton bras.")
        }
    }

    private fun hasArmLandmarks(result: PoseLandmarkerResult): Boolean {
        val poses = result.landmarks()
        if (poses.isEmpty()) return false

        val lm = poses[0]

        fun visibilityAt(index: Int): Float {
            val opt = lm[index].visibility()
            return if (opt.isPresent) opt.get() else 1f
        }

        val leftOk =
            visibilityAt(LEFT_SHOULDER) > 0.5f &&
            visibilityAt(LEFT_ELBOW) > 0.5f &&
            visibilityAt(LEFT_WRIST) > 0.5f

        val rightOk =
            visibilityAt(RIGHT_SHOULDER) > 0.5f &&
            visibilityAt(RIGHT_ELBOW) > 0.5f &&
            visibilityAt(RIGHT_WRIST) > 0.5f

        return leftOk || rightOk
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
