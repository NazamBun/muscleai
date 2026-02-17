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
import com.nazam.muscleai.domain.profile.UserProfile
import org.jetbrains.compose.resources.stringResource

import muscleai.composeapp.generated.resources.Res
import muscleai.composeapp.generated.resources.error_age_range
import muscleai.composeapp.generated.resources.error_days_range
import muscleai.composeapp.generated.resources.error_fill_all_fields
import muscleai.composeapp.generated.resources.error_height_range
import muscleai.composeapp.generated.resources.error_weight_range
import muscleai.composeapp.generated.resources.q_age
import muscleai.composeapp.generated.resources.q_days_per_week
import muscleai.composeapp.generated.resources.q_has_dumbbells
import muscleai.composeapp.generated.resources.q_height_cm
import muscleai.composeapp.generated.resources.q_submit
import muscleai.composeapp.generated.resources.q_weight_kg

@Composable
fun QuestionsScreen(
    onSubmit: (UserProfile) -> Unit
) {
    var state by remember { mutableStateOf(QuestionsUiState()) }

    val errFill = stringResource(Res.string.error_fill_all_fields)
    val errAge = stringResource(Res.string.error_age_range)
    val errHeight = stringResource(Res.string.error_height_range)
    val errWeight = stringResource(Res.string.error_weight_range)
    val errDays = stringResource(Res.string.error_days_range)

    fun errorText(key: ErrorKey): String = when (key) {
        ErrorKey.FILL_ALL -> errFill
        ErrorKey.AGE_RANGE -> errAge
        ErrorKey.HEIGHT_RANGE -> errHeight
        ErrorKey.WEIGHT_RANGE -> errWeight
        ErrorKey.DAYS_RANGE -> errDays
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = state.age,
            onValueChange = { state = state.copy(age = it, error = "") },
            label = { Text(stringResource(Res.string.q_age)) }
        )
        OutlinedTextField(
            value = state.heightCm,
            onValueChange = { state = state.copy(heightCm = it, error = "") },
            label = { Text(stringResource(Res.string.q_height_cm)) }
        )
        OutlinedTextField(
            value = state.weightKg,
            onValueChange = { state = state.copy(weightKg = it, error = "") },
            label = { Text(stringResource(Res.string.q_weight_kg)) }
        )
        OutlinedTextField(
            value = state.daysPerWeek,
            onValueChange = { state = state.copy(daysPerWeek = it, error = "") },
            label = { Text(stringResource(Res.string.q_days_per_week)) }
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(Res.string.q_has_dumbbells))
            Checkbox(
                checked = state.hasDumbbells,
                onCheckedChange = { state = state.copy(hasDumbbells = it) }
            )
        }

        if (state.error.isNotBlank()) {
            Text(state.error)
        }

        Button(
            onClick = {
                when (val result = QuestionsValidator.validate(state)) {
                    is ValidationResult.Ok -> onSubmit(result.profile)
                    is ValidationResult.Error -> state = state.copy(error = errorText(result.errorKey))
                }
            }
        ) {
            Text(stringResource(Res.string.q_submit))
        }
    }
}
