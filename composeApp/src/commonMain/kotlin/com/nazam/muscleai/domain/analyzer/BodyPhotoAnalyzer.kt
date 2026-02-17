package com.nazam.muscleai.domain.analyzer

/**
 * Contrat "KMP friendly".
 * Android et iOS auront chacun leur implémentation plus tard.
 */
interface BodyPhotoAnalyzer {
    suspend fun analyze(part: BodyPart): PhotoAnalysisResult
}
