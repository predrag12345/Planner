package com.example.planner.presentation.login

import com.example.planner.presentation.ViewEvent

sealed class LoginScreenEvent : ViewEvent {
    data class UsernameChanged(val username: String) : LoginScreenEvent()
    data class PasswordChanged(val password: String) : LoginScreenEvent()
    data class LoginButtonPressed(val username: String, val password: String) : LoginScreenEvent()
    data class SignUpButtonPressed(val unusedParameter: Unit = Unit) : LoginScreenEvent()




}