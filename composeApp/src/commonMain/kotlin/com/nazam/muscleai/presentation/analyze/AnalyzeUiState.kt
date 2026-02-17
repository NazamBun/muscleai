package com.nazam.muscleai.presentation.analyze

data class AnalyzeUiState(
    val title: String,
    val description: String,
    val buttonText: String,
    val statusText: String,
    val isLoading: Boolean
) {
    companion object {
        fun initial(title: String, description: String, buttonText: String) = AnalyzeUiState(
            title = title,
            description = description,
            buttonText = buttonText,
            statusText = "",
            isLoading = false
        )
    }
}
