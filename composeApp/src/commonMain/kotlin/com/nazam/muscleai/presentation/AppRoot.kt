package com.nazam.muscleai.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.nazam.muscleai.presentation.analyze.AnalyzeScreen

@Composable
fun AppRoot() {
    MaterialTheme {
        AnalyzeScreen()
    }
}
