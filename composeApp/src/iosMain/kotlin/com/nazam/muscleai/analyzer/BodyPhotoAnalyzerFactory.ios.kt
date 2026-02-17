package com.nazam.muscleai.domain.analyzer

import kotlinx.coroutines.delay

actual class BodyPhotoAnalyzerFactory {
    actual fun create(): BodyPhotoAnalyzer = IosBodyPhotoAnalyzer()
}

private class IosBodyPhotoAnalyzer : BodyPhotoAnalyzer {
    override suspend fun analyze(part: BodyPart): PhotoAnalysisResult {
        delay(300)
        return PhotoAnalysisResult(
            isValid = true,
            message = "iOS: analyse locale prête (stub)."
        )
    }
}
