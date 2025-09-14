package com.example.planner.presentation.registration


import android.content.Context
import android.widget.Toast
import androidx.lifecycle.viewModelScope
import com.example.planner.domains.model.ValidationResult
import com.example.planner.domains.useCase.RegisterUserUseCase
import com.example.planner.domains.useCase.ValidateRegistrationInputUseCase
import com.example.planner.presentation.registration.RegistrationScreenEvent
import com.example.planner.presentation.BaseViewModel
import com.example.planner.presentation.ViewSideEffect
import com.example.planner.presentation.ViewState


import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val validateRegistrationInputUseCase: ValidateRegistrationInputUseCase,
    @ApplicationContext private val context: Context
): BaseViewModel<RegistrationScreenEvent, RegistrationScreenState, RegistrationScreenEffect>() {

    override fun setInitialState(): RegistrationScreenState {
        return RegistrationScreenState(
            firstname = "",
            lastname = "",
            username = "",
            password = "",
            error = null
        )
    }

    override fun handleEvents(event: RegistrationScreenEvent) {
        when (event) {
            is RegistrationScreenEvent.FirstnameChanged -> {
                setState {
                    copy(firstname = event.firstname, error = null)

                }

            }
            is RegistrationScreenEvent.LastnameChanged -> {
                setState {
                    copy(lastname = event.lastname, error = null)

                }

            }
            is RegistrationScreenEvent.UsernameChanged -> {
                setState {
                    copy(username = event.username, error = null)

                }

            }
            is RegistrationScreenEvent.PasswordChanged -> {
                setState {
                    copy(password = event.password, error = null)
                }
            }
            is RegistrationScreenEvent.LoginButtonPressed -> {
                handleRegistration(event.firstname,event.lastname,event.username, event.password)
            }

            is RegistrationScreenEvent.SignUpButtonPressed -> loadNextScreenLogin()


        }
    }

    private fun handleRegistration(firstname: String,lastname: String,username: String, password: String) {
        viewModelScope.launch {


            val authResult = validateRegistrationInputUseCase.valid(firstname, lastname, username, password)
            if (authResult is ValidationResult.Success) {



                runCatching<ValidationResult> {
                    registerUserUseCase.performRegister(firstname, lastname, username, password)
                }.onSuccess { verification ->
                    when (verification) {
                        is ValidationResult.Success -> {
                            loadNextScreenDashboard()
                        }
                        is ValidationResult.Error -> {
                            Toast.makeText(getApplication(context), verification.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }.onFailure { e ->
                    Toast.makeText(getApplication(context), "An error occurred. Please try again.", Toast.LENGTH_LONG).show()
                    e.printStackTrace()
                }


            }
            else if (authResult is ValidationResult.Error) {
                Toast.makeText(getApplication(context), authResult.message, Toast.LENGTH_LONG).show()
            }

        }
    }




    private fun loadNextScreenDashboard() {
        viewModelScope.launch {
            setEffect {
                RegistrationScreenEffect.NavigateToDashboardScreen
            }
        }
    }
    private fun loadNextScreenLogin() {
        viewModelScope.launch {
            setEffect { RegistrationScreenEffect.NavigateToLoginScreen }
        }
    }

}

data class RegistrationScreenState(
    val firstname: String,
    val lastname: String,
    val username: String,
    val password: String,
    val error: String?
) : ViewState

sealed class RegistrationScreenEffect : ViewSideEffect {

    object NavigateToLoginScreen : RegistrationScreenEffect()
    object NavigateToDashboardScreen : RegistrationScreenEffect()

}