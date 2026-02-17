package com.nazam.muscleai.presentation.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nazam.muscleai.domain.analyzer.PhotoInput

@Composable
actual fun CameraCapture(
    onPhoto: (PhotoInput) -> Unit,
    onCancel: () -> Unit
) {
    // iOS sera fait ensuite (picker / caméra)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Caméra iOS: pas encore disponible")
        Button(onClick = onCancel) { Text("Retour") }
    }
}
