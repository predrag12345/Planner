package com.example.planner.presentation.setPlan

import android.app.TimePickerDialog
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planner.presentation.bottomNavigationBar.BottomNavigationBar
import java.time.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SetPlanScreen(
    navController: NavController,
    viewModel: SetPlanViewModel = hiltViewModel()
) {
    val state by viewModel.viewState
    val context = LocalContext.current
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Create New Plan") })
        },
        bottomBar = {
            BottomNavigationBar(navController, 2)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { viewModel.setEvent(SetPlanEvent.TitleChanged(it)) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = { viewModel.setEvent(SetPlanEvent.DescriptionChanged(it)) },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = state.startDate?.format(dateTimeFormatter) ?: "",
                onValueChange = {},
                label = { Text("Start Date & Time") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showStartDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick start date")
                    }
                }
            )

            OutlinedTextField(
                value = state.endDate?.format(dateTimeFormatter) ?: "",
                onValueChange = {},
                label = { Text("End Date & Time") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showEndDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "Pick end date")
                    }
                }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = state.notificationEnabled,
                    onCheckedChange = { viewModel.setEvent(SetPlanEvent.NotificationToggled(it)) }
                )
                Text("Enable Notification Alert")
            }

            Button(
                onClick = { viewModel.setEvent(SetPlanEvent.SavePlan) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF4A7DDF), Color(0xFF4AD6DF)),
                            startY = 0.0f,
                            endY = 150.0f
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Text("Save")
            }
        }
    }

    // Start date picker
    if (showStartDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showStartDatePicker = false }) { Text("OK") }
            }
        ) {
            val datePickerState = rememberDatePickerState()
            DatePicker(state = datePickerState)

            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    val pickedDate = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    showTimePicker(context, LocalTime.now()) { hour, minute ->
                        val dateTime = LocalDateTime.of(pickedDate, LocalTime.of(hour, minute))
                        viewModel.setEvent(SetPlanEvent.StartDateChanged(dateTime))
                    }
                    showStartDatePicker = false
                }
            }
        }
    }

    // End date picker
    if (showEndDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showEndDatePicker = false }) { Text("OK") }
            }
        ) {
            val datePickerState = rememberDatePickerState()
            DatePicker(state = datePickerState)

            LaunchedEffect(datePickerState.selectedDateMillis) {
                datePickerState.selectedDateMillis?.let { millis ->
                    val pickedDate = Instant.ofEpochMilli(millis)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()

                    showTimePicker(context, LocalTime.now()) { hour, minute ->
                        val dateTime = LocalDateTime.of(pickedDate, LocalTime.of(hour, minute))
                        viewModel.setEvent(SetPlanEvent.EndDateChanged(dateTime))
                    }
                    showEndDatePicker = false
                }
            }
        }
    }

    // side effects
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SetPlanEffect.PlanSaved -> {
                    Toast.makeText(context, "Plan saved successfully!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
                SetPlanEffect.PlanSaveError -> {
                    Toast.makeText(context, "Error saving plan.", Toast.LENGTH_SHORT).show()
                }
                is SetPlanEffect.ValidationError -> {
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun showTimePicker(
    context: android.content.Context,
    initialTime: LocalTime,
    onTimeSelected: (Int, Int) -> Unit
) {
    TimePickerDialog(
        context,
        { _, hour: Int, minute: Int -> onTimeSelected(hour, minute) },
        initialTime.hour,
        initialTime.minute,
        true
    ).show()
}
