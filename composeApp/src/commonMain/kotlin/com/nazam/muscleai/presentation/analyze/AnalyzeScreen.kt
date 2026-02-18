package com.nazam.muscleai.presentation.analyze

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.stringResource
import com.nazam.muscleai.presentation.camera.CameraCapture
import com.nazam.muscleai.presentation.analyze.questions.QuestionsScreen
import muscleai.composeapp.generated.resources.*

private enum class Step { PHOTO, CAMERA, QUESTIONS, RESULT }

@Composable
fun AnalyzeScreen(
    vm: AnalyzeViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()
    val resultState by vm.resultState.collectAsState()
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

            Button(onClick = { step = Step.CAMERA }) {
                Text(buttonText)
            }

            if (state.isLoading) CircularProgressIndicator()

            if (state.scoreText.isNotBlank()) Text(state.scoreText)
            if (state.statusText.isNotBlank()) Text(state.statusText)
        }

        Step.CAMERA -> CameraCapture(
            onPhoto = {
                vm.onPhotoCaptured(it)
                step = Step.QUESTIONS
            },
            onCancel = { step = Step.PHOTO }
        )

        Step.QUESTIONS -> QuestionsScreen(
            onSubmit = { profile ->
                vm.generatePlan(profile)
                step = Step.RESULT
            }
        )

        Step.RESULT -> Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(resultState.planTitle)
            resultState.exercises.forEach { Text("- $it") }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { step = Step.PHOTO }) {
                Text(stringResource(Res.string.restart))
            }
        }
    }
}
