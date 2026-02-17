package com.nazam.muscleai.presentation.analyze

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nazam.muscleai.domain.analyzer.BodyPart
import com.nazam.muscleai.domain.analyzer.BodyPhotoAnalyzer
import com.nazam.muscleai.domain.analyzer.BodyPhotoAnalyzerFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AnalyzeViewModel(
    private val analyzer: BodyPhotoAnalyzer = BodyPhotoAnalyzerFactory().create()
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        AnalyzeUiState.initial(title = "", description = "", buttonText = "")
    )
    val uiState: StateFlow<AnalyzeUiState> = _uiState

    fun initTexts(title: String, description: String, buttonText: String) {
        if (_uiState.value.title.isNotBlank()) return
        _uiState.value = AnalyzeUiState.initial(title, description, buttonText)
    }

    fun onTakePhotoClicked() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, statusText = "")
            val result = analyzer.analyze(BodyPart.ARM)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                statusText = result.message
            )
        }
    }
}
