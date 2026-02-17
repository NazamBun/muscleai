package com.nazam.muscleai.presentation.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nazam.muscleai.domain.analyzer.BodyPhotoAnalyzer
import com.nazam.muscleai.domain.analyzer.BodyPhotoAnalyzerFactory
import com.nazam.muscleai.domain.analyzer.PhotoInput
import com.nazam.muscleai.domain.profile.UserProfile
import com.nazam.muscleai.domain.usecase.GenerateBicepsPlanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ResultState(
    val planTitle: String = "",
    val exercises: List<String> = emptyList()
)

class AnalyzeViewModel(
    private val analyzer: BodyPhotoAnalyzer = BodyPhotoAnalyzerFactory().create(),
    private val generatePlan: GenerateBicepsPlanUseCase = GenerateBicepsPlanUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyzeUiState.initial("", "", ""))
    val uiState: StateFlow<AnalyzeUiState> = _uiState

    private val _resultState = MutableStateFlow(ResultState())
    val resultState: StateFlow<ResultState> = _resultState

    fun initTexts(title: String, description: String, buttonText: String) {
        if (_uiState.value.title.isNotBlank()) return
        _uiState.value = AnalyzeUiState.initial(title, description, buttonText)
    }

    fun onPhotoCaptured(photo: PhotoInput) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, statusText = "")
            val result = analyzer.analyze(photo)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                statusText = result.message
            )
        }
    }

    fun generatePlan(profile: UserProfile) {
        val plan = generatePlan.execute(profile)
        _resultState.value = ResultState(planTitle = plan.title, exercises = plan.items)
    }
}
