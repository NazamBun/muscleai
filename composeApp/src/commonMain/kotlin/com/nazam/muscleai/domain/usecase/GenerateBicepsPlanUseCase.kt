package com.nazam.muscleai.domain.usecase

import com.nazam.muscleai.domain.profile.UserProfile
import com.nazam.muscleai.domain.workout.WorkoutPlan

class GenerateBicepsPlanUseCase {

    fun execute(profile: UserProfile): WorkoutPlan {

        val intensity = when {
            profile.trainingDaysPerWeek <= 2 -> "Débutant"
            profile.trainingDaysPerWeek <= 4 -> "Intermédiaire"
            else -> "Avancé"
        }

        val exercises = buildList {
            add("Curl haltères - 4x10")
            add("Curl marteau - 3x12")

            if (profile.hasDumbbells) {
                add("Curl concentration - 3x10")
            } else {
                add("Pompes prise serrée - 4x12")
            }

            if (profile.trainingDaysPerWeek >= 4) {
                add("Curl barre lourd - 5x5")
            }
        }

        return WorkoutPlan(
            title = "Plan Biceps ($intensity)",
            items = exercises
        )
    }
}
