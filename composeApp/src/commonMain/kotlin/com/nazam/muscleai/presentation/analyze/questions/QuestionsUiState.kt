package com.nazam.muscleai.presentation.analyze.questions

data class QuestionsUiState(
    val age: String = "",
    val heightCm: String = "",
    val weightKg: String = "",
    val daysPerWeek: String = "3",
    val hasDumbbells: Boolean = false,
    val error: String = ""
)
