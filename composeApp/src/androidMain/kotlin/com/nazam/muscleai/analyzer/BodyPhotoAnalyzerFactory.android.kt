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
import kotlin.math.sqrt

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
        delay(120)

        val bitmap = photo.bitmap

        when {
            PhotoQuality.isTooSmall(bitmap) -> return PhotoAnalysisResult(false, "Photo trop petite.", 0)
            PhotoQuality.isTooDark(bitmap) -> return PhotoAnalysisResult(false, "Photo trop sombre.", 0)
            PhotoQuality.isTooBright(bitmap) -> return PhotoAnalysisResult(false, "Photo trop lumineuse.", 0)
            PhotoQuality.isTooBlurry(bitmap) -> return PhotoAnalysisResult(false, "Photo floue. Reste immobile.", 0)
        }

        val mpImage = BitmapImageBuilder(bitmap).build()
        val result = landmarker.detect(mpImage)

        val arm = bestArm(result) ?: return PhotoAnalysisResult(
            isValid = false,
            message = "Je ne vois pas le corps. Mets-toi face à la caméra.",
            score = 0
        )

        val score = (arm.avgVis * 100f).roundToInt().coerceIn(0, 100)

        val tips = buildTips(arm)

        val ok = score >= 60
        val message = if (ok) {
            if (tips.isBlank()) "Bras bien cadré ✅"
            else "Bras détecté ✅ $tips"
        } else {
            "Je ne vois pas bien le bras. $tips"
        }

        return PhotoAnalysisResult(ok, message, score)
    }

    private data class ArmData(
        val avgVis: Float,
        val shoulderX: Float,
        val shoulderY: Float,
        val elbowX: Float,
        val elbowY: Float,
        val wristX: Float,
        val wristY: Float
    )

    private fun bestArm(result: PoseLandmarkerResult): ArmData? {
        val poses = result.landmarks()
        if (poses.isEmpty()) return null
        val lm = poses[0]

        fun vis(i: Int): Float {
            val opt = lm[i].visibility()
            return if (opt.isPresent) opt.get() else 0f
        }

        fun x(i: Int): Float = lm[i].x()
        fun y(i: Int): Float = lm[i].y()

        val leftVis = (vis(LS) + vis(LE) + vis(LW)) / 3f
        val rightVis = (vis(RS) + vis(RE) + vis(RW)) / 3f

        return if (leftVis >= rightVis && leftVis > 0f) {
            ArmData(
                avgVis = leftVis,
                shoulderX = x(LS), shoulderY = y(LS),
                elbowX = x(LE), elbowY = y(LE),
                wristX = x(LW), wristY = y(LW)
            )
        } else if (rightVis > 0f) {
            ArmData(
                avgVis = rightVis,
                shoulderX = x(RS), shoulderY = y(RS),
                elbowX = x(RE), elbowY = y(RE),
                wristX = x(RW), wristY = y(RW)
            )
        } else null
    }

    private fun buildTips(arm: ArmData): String {
        // Coordonnées normalisées : x [0..1], y [0..1] (y vers le bas)
        val wristX = arm.wristX
        val wristY = arm.wristY

        val tips = mutableListOf<String>()

        // Gauche / droite
        when {
            wristX < 0.30f -> tips += "Décale ton bras vers la droite."
            wristX > 0.70f -> tips += "Décale ton bras vers la gauche."
        }

        // Haut / bas
        when {
            wristY < 0.25f -> tips += "Baisse un peu ton bras."
            wristY > 0.75f -> tips += "Monte un peu ton bras."
        }

        // Trop loin (bras trop petit à l’écran) : distance épaule→poignet
        val dx = arm.wristX - arm.shoulderX
        val dy = arm.wristY - arm.shoulderY
        val dist = sqrt(dx * dx + dy * dy)

        if (dist < 0.18f) tips += "Rapproche-toi un peu."

        // Si rien à dire
        return tips.take(3).joinToString(" ")
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
