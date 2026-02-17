package com.nazam.muscleai.data.analyzer

import com.nazam.muscleai.domain.analyzer.BodyPart
import com.nazam.muscleai.domain.analyzer.BodyPhotoAnalyzer
import com.nazam.muscleai.domain.analyzer.PhotoAnalysisResult
import kotlinx.coroutines.delay

class FakeBodyPhotoAnalyzer : BodyPhotoAnalyzer {

    override suspend fun analyze(part: BodyPart): PhotoAnalysisResult {
        delay(400) // simule un "temps d'analyse"
        return PhotoAnalysisResult(
            isValid = true,
            message = "Photo OK: on peut continuer."
        )
    }
}
