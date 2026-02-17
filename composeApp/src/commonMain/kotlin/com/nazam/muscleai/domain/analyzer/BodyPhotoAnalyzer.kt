package com.nazam.muscleai.domain.analyzer

interface BodyPhotoAnalyzer {
    suspend fun analyze(photo: PhotoInput): PhotoAnalysisResult
}
