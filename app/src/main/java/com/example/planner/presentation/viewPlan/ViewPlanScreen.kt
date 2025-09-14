package com.example.planner.presentation.viewPlan

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planner.presentation.bottomNavigationBar.BottomNavigationBar
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ViewPlanScreen(
    navController: NavController,
    title: String,
    description: String,
    startDate: String,
    endDate: String,
    viewModel: ViewPlanViewModel = hiltViewModel()
) {
    val state by viewModel.viewState
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    LaunchedEffect(Unit) {
        viewModel.setEvent(ViewPlanEvent.LoadPlan(title, description, startDate, endDate))
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Plan Info") }) },
        bottomBar = { BottomNavigationBar(navController, 0) }
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
                onValueChange = {},
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = {},
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            OutlinedTextField(
                value = state.startDate?.format(dateTimeFormatter) ?: "",
                onValueChange = {},
                label = { Text("Start Date & Time") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )

            OutlinedTextField(
                value = state.endDate?.format(dateTimeFormatter) ?: "",
                onValueChange = {},
                label = { Text("End Date & Time") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true
            )
        }
    }
}
