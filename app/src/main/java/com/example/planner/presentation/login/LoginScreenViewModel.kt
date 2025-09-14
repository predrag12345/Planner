package com.example.planner.presentation.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.planner.domains.useCase.ValidLoginFieldsUseCase
import com.example.planner.domains.model.ValidationResult

import com.example.planner.presentation.BaseViewModel
import com.example.planner.presentation.ViewSideEffect
import com.example.planner.presentation.ViewState
import com.example.planner.domains.useCase.LoginUseCase


import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val loginVerification: LoginUseCase,
    private val loginAuth: ValidLoginFieldsUseCase,
    @ApplicationContext private val context: Context
): BaseViewModel<LoginScreenEvent, LoginScreenState, LoginScreenEffect>() {

    override fun setInitialState(): LoginScreenState {
        return LoginScreenState(
            username = "",
            password = "",
            error = null
        )
    }

    override fun handleEvents(event: LoginScreenEvent) {
        when (event) {
            is LoginScreenEvent.UsernameChanged -> {
                setState {
                    copy(username = event.username, error = null)

                }

            }
            is LoginScreenEvent.PasswordChanged -> {
                setState {
                    copy(password = event.password, error = null)
                }
            }
            is LoginScreenEvent.LoginButtonPressed -> {
                handleLogin(event.username, event.password)
            }

            is LoginScreenEvent.SignUpButtonPressed -> loadNextScreenRegister()

        }
    }

    private fun handleLogin(username: String, password: String) {
        viewModelScope.launch {
            val authResult = loginAuth.authenticate(username, password)

            if (authResult is ValidationResult.Success) {
                try {
                    val verification = loginVerification.performLogin(username, password)

                       if(verification is ValidationResult.Success){
                           loadNextScreenDashboard()
                       }
                    else if(verification is ValidationResult.Error){

                           Toast.makeText(getApplication(context), verification.message, Toast.LENGTH_LONG).show()

                    }

                } catch (e: Exception) {
                    Toast.makeText(getApplication(context), "An error occurred. Please try again.", Toast.LENGTH_LONG).show()
                }
            } else if (authResult is ValidationResult.Error) {
                Toast.makeText(getApplication(context), authResult.message, Toast.LENGTH_LONG).show()
            }
        }
    }




    private fun loadNextScreenDashboard() {
        viewModelScope.launch {
            setEffect {
                LoginScreenEffect.NavigateToDashboardScreen
            }
        }
    }
    private fun loadNextScreenRegister() {
        viewModelScope.launch {
            setEffect { LoginScreenEffect.NavigateToRegisterScreen }
        }
    }

}

data class LoginScreenState(
    val username: String,
    val password: String,
    val error: String?
) : ViewState

sealed class LoginScreenEffect : ViewSideEffect {

    object NavigateToRegisterScreen : LoginScreenEffect()
    object NavigateToDashboardScreen : LoginScreenEffect()

}

