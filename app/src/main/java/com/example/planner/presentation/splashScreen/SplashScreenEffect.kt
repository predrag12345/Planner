package com.example.planner.presentation.splashScreen

import com.example.planner.presentation.ViewSideEffect

sealed class SplashScreenEffect: ViewSideEffect {
    object NavigateToDashboardScreen : SplashScreenEffect()
    object NavigateToLoginScreen : SplashScreenEffect()
}
