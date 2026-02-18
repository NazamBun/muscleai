package com.nazam.muscleai.domain.analyzer

data class PhotoAnalysisResult(
    val isValid: Boolean,
    val message: String,
    val score: Int? = null // 0..100, null si pas dispo
)
