package com.nazam.muscleai.presentation.analyze

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    val state = vm.uiState(
        title = stringResource(Res.string.analyze_title),
        description = stringResource(Res.string.analyze_description)
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(state.title)
        Text(state.description)

        Button(onClick = { }) {
            Text(stringResource(Res.string.analyze_cta))
        }
    }
}
