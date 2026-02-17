package com.nazam.muscleai.presentation.camera

import androidx.compose.runtime.Composable
import com.nazam.muscleai.domain.analyzer.PhotoInput

@Composable
expect fun CameraCapture(
    onPhoto: (PhotoInput) -> Unit,
    onCancel: () -> Unit
)
