package com.nazam.muscleai.presentation.analyze

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.stringResource

import com.nazam.muscleai.presentation.analyze.questions.QuestionsScreen
import muscleai.composeapp.generated.resources.Res
import muscleai.composeapp.generated.resources.analyze_cta
import muscleai.composeapp.generated.resources.analyze_description
import muscleai.composeapp.generated.resources.analyze_title
import muscleai.composeapp.generated.resources.restart
import muscleai.composeapp.generated.resources.result_title

private enum class Step { PHOTO, QUESTIONS, RESULT }

@Composable
fun AnalyzeScreen(
    vm: AnalyzeViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    var step by remember { mutableStateOf(Step.PHOTO) }

    val title = stringResource(Res.string.analyze_title)
    val description = stringResource(Res.string.analyze_description)
    val buttonText = stringResource(Res.string.analyze_cta)

    LaunchedEffect(title, description, buttonText) {
        vm.initTexts(title, description, buttonText)
    }

    when (step) {
        Step.PHOTO -> Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(state.title)
            Text(state.description)

            Button(
                onClick = {
                    vm.onTakePhotoClicked()
                    step = Step.QUESTIONS
                },
                enabled = !state.isLoading
            ) {
                Text(state.buttonText)
            }

            if (state.isLoading) CircularProgressIndicator()
            if (state.statusText.isNotBlank()) Text(state.statusText)
        }

        Step.QUESTIONS -> QuestionsScreen(
            onSubmit = { step = Step.RESULT }
        )

        Step.RESULT -> Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(Res.string.result_title))
            Button(onClick = { step = Step.PHOTO }) {
                Text(stringResource(Res.string.restart))
            }
        }
    }
}
