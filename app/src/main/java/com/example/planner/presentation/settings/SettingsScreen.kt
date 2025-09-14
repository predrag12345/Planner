package com.example.planner.presentation.settings

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.planner.presentation.bottomNavigationBar.BottomNavigationBar
import com.example.wollyapp.presentation.showDialog.ShowDialogScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) },
                bottomBar = { BottomNavigationBar(navController, 3) }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 40.dp, start = 40.dp, end = 40.dp)
        ) {
            Button(
                onClick = { viewModel.handleEvents(SettingsScreenEvent.LogoutButtonPressed) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(vertical = 2.dp)
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color(0xFF4A7DDF), Color(0xFF4AD6DF)),
                            startY = 0.0f,
                            endY = 150.0f
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Text(
                    text = "Logout",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }

            if (viewModel.viewState.value.isLogoutDialogVisible) {
                ShowDialogScreen(
                    message = "Are you sure you want to logout?",
                    onConfirmPressed = { viewModel.setEvent(SettingsScreenEvent.ConfirmButtonPressed) },
                    onCancelPressed = { viewModel.setEvent(SettingsScreenEvent.CancelButtonPressed) }
                )
            }
        }


    }

    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsScreenEffect.NavigateToLoginScreen -> {
                    navController.navigate("login")
                }
                SettingsScreenEffect.HandleError -> {
                    Toast.makeText(
                        context,
                        "An error occurred. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}












