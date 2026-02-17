package com.nazam.muscleai.presentation.analyze

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.stringResource

import muscleai.composeapp.generated.resources.Res
import muscleai.composeapp.generated.resources.analyze_cta
import muscleai.composeapp.generated.resources.analyze_description
import muscleai.composeapp.generated.resources.analyze_title

@Composable
fun AnalyzeScreen(
    vm: AnalyzeViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    // ✅ stringResource est appelé dans le Composable (OK)
    val title = stringResource(Res.string.analyze_title)
    val description = stringResource(Res.string.analyze_description)
    val buttonText = stringResource(Res.string.analyze_cta)

    // ✅ Ici on passe juste des Strings (OK)
    LaunchedEffect(title, description, buttonText) {
        vm.initTexts(
            title = title,
            description = description,
            buttonText = buttonText
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(state.title)
        Text(state.description)

        Button(
            onClick = vm::onTakePhotoClicked,
            enabled = !state.isLoading
        ) {
            Text(state.buttonText)
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }

        if (state.statusText.isNotBlank()) {
            Text(state.statusText)
        }
    }
}
