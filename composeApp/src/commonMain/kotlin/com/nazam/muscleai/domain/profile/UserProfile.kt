package com.nazam.muscleai.domain.profile

data class UserProfile(
    val age: Int,
    val heightCm: Int,
    val weightKg: Int,
    val trainingDaysPerWeek: Int,
    val hasDumbbells: Boolean
)
