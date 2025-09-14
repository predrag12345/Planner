package com.example.planner.presentation.splashScreen

import androidx.lifecycle.viewModelScope
import com.example.planner.domains.useCase.CheckIfUserIsLoggedUseCase
import com.example.planner.presentation.BaseViewModel
import com.example.planner.presentation.ViewEvent
import com.example.planner.presentation.ViewSideEffect
import com.example.planner.presentation.ViewState
import com.google.firebase.auth.FirebaseAuth

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val auth: FirebaseAuth
) : BaseViewModel<SplashScreenEvent, SplashScreenState, SplashScreenEffect>() {

    override fun setInitialState(): SplashScreenState = SplashScreenState()

    override fun handleEvents(event: SplashScreenEvent) {
        when (event) {
            SplashScreenEvent.LoadNextScreen -> {
                viewModelScope.launch {
                    delay(1500)
                    if (auth.currentUser != null) {
                        setEffect { SplashScreenEffect.NavigateToDashboardScreen }
                    } else {
                        setEffect { SplashScreenEffect.NavigateToLoginScreen }
                    }
                }
            }
        }
    }
}

data class SplashScreenState(
    val dummy: Boolean = false
) : ViewState

sealed class SplashScreenEvent: ViewEvent {
    object LoadNextScreen : SplashScreenEvent()
}


