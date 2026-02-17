package com.nazam.muscleai.domain.analyzer

import kotlinx.coroutines.delay

actual class BodyPhotoAnalyzerFactory {
    actual fun create(): BodyPhotoAnalyzer = AndroidBodyPhotoAnalyzer()
}

private class AndroidBodyPhotoAnalyzer : BodyPhotoAnalyzer {
    override suspend fun analyze(part: BodyPart): PhotoAnalysisResult {
        delay(300)
        return PhotoAnalysisResult(
            isValid = true,
            message = "Android: analyse locale prête (stub)."
        )
    }
}
