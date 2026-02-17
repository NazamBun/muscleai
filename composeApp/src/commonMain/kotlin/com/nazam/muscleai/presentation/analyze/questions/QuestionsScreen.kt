package com.nazam.muscleai.presentation.analyze.questions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionsScreen(
    onSubmit: (QuestionsUiState) -> Unit
) {
    var state by remember { mutableStateOf(QuestionsUiState()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.age,
            onValueChange = { state = state.copy(age = it, error = "") },
            label = { Text("Âge") }
        )
        OutlinedTextField(
            value = state.heightCm,
            onValueChange = { state = state.copy(heightCm = it, error = "") },
            label = { Text("Taille (cm)") }
        )
        OutlinedTextField(
            value = state.weightKg,
            onValueChange = { state = state.copy(weightKg = it, error = "") },
            label = { Text("Poids (kg)") }
        )
        OutlinedTextField(
            value = state.daysPerWeek,
            onValueChange = { state = state.copy(daysPerWeek = it, error = "") },
            label = { Text("Jours / semaine") }
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Tu as des haltères ?")
            Checkbox(
                checked = state.hasDumbbells,
                onCheckedChange = { state = state.copy(hasDumbbells = it) }
            )
        }

        if (state.error.isNotBlank()) {
            Text(state.error)
        }

        Button(onClick = { onSubmit(state) }) {
            Text("Valider")
        }
    }
}
