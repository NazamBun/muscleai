package com.nazam.muscleai.domain.analyzer

/**
 * Permet de créer l'analyseur selon la plateforme (Android / iOS).
 * commonMain ne connaît pas MediaPipe/Vision.
 */
expect class BodyPhotoAnalyzerFactory() {
    fun create(): BodyPhotoAnalyzer
}
