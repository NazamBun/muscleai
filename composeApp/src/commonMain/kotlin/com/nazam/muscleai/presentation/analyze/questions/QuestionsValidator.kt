package com.nazam.muscleai.presentation.analyze.questions

import com.nazam.muscleai.domain.profile.UserProfile

sealed class ValidationResult {
    data class Ok(val profile: UserProfile) : ValidationResult()
    data class Error(val errorKey: ErrorKey) : ValidationResult()
}

enum class ErrorKey {
    FILL_ALL,
    AGE_RANGE,
    HEIGHT_RANGE,
    WEIGHT_RANGE,
    DAYS_RANGE
}

object QuestionsValidator {

    fun validate(state: QuestionsUiState): ValidationResult {
        val age = state.age.toIntOrNull() ?: return ValidationResult.Error(ErrorKey.FILL_ALL)
        val height = state.heightCm.toIntOrNull() ?: return ValidationResult.Error(ErrorKey.FILL_ALL)
        val weight = state.weightKg.toIntOrNull() ?: return ValidationResult.Error(ErrorKey.FILL_ALL)
        val days = state.daysPerWeek.toIntOrNull() ?: return ValidationResult.Error(ErrorKey.FILL_ALL)

        if (age !in 5..100) return ValidationResult.Error(ErrorKey.AGE_RANGE)
        if (height !in 80..250) return ValidationResult.Error(ErrorKey.HEIGHT_RANGE)
        if (weight !in 20..250) return ValidationResult.Error(ErrorKey.WEIGHT_RANGE)
        if (days !in 1..7) return ValidationResult.Error(ErrorKey.DAYS_RANGE)

        return ValidationResult.Ok(
            UserProfile(
                age = age,
                heightCm = height,
                weightKg = weight,
                trainingDaysPerWeek = days,
                hasDumbbells = state.hasDumbbells
            )
        )
    }
}
