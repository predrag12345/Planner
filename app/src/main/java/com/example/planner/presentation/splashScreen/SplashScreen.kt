package com.example.planner.presentation.splashScreen

import android.window.SplashScreen
import android.window.SplashScreenView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.planner.R
import com.example.planner.presentation.BaseViewModel
@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashScreenViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF4A7DDF), Color(0xFF4AD6DF)),
                    startY = 0.0f,
                    endY = 1500.0f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_planner1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            )
            Text(
                modifier = Modifier.alpha(0.9f),
                text = "FocusFlow",
                fontSize = 36.sp,
                color = Color.White
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SplashScreenEffect.NavigateToLoginScreen -> {
                    navController.navigate("login") {
                        popUpTo("splashScreen") { inclusive = true }
                    }
                }

                SplashScreenEffect.NavigateToDashboardScreen -> {
                    navController.navigate("dashboard") {
                        popUpTo("splashScreen") { inclusive = true }
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setEvent(SplashScreenEvent.LoadNextScreen)
    }
}



@Preview
@Composable
fun SplashScreenPreview() {
    SplashScreen(  navController= rememberNavController())
}