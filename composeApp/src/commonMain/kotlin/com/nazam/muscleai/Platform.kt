package com.nazam.muscleai

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform