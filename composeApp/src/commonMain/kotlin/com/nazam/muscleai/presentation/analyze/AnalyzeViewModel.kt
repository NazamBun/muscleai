package com.nazam.muscleai.presentation.analyze

import androidx.lifecycle.ViewModel

class AnalyzeViewModel : ViewModel() {

    fun uiState(title: String, description: String): AnalyzeUiState {
        return AnalyzeUiState(
            title = title,
            description = description
        )
    }
}
