package com.nazam.muscleai.domain.analyzer

import kotlinx.coroutines.delay

actual class BodyPhotoAnalyzerFactory {
    actual fun create(): BodyPhotoAnalyzer = IosBodyPhotoAnalyzer()
}

private class IosBodyPhotoAnalyzer : BodyPhotoAnalyzer {
    override suspend fun analyze(photo: PhotoInput): PhotoAnalysisResult {
        delay(500)
        return PhotoAnalysisResult(
            isValid = true,
            message = "iOS: photo reçue, analyse locale OK."
        )
    }
}
