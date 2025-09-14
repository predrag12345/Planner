package com.example.planner.presentation.registration

import com.example.planner.presentation.ViewEvent

sealed class RegistrationScreenEvent : ViewEvent {
    data class FirstnameChanged(val firstname: String) : RegistrationScreenEvent()
    data class LastnameChanged(val lastname: String) : RegistrationScreenEvent()
    data class UsernameChanged(val username: String) : RegistrationScreenEvent()
    data class PasswordChanged(val password: String) : RegistrationScreenEvent()
    data class LoginButtonPressed(val firstname:String,val lastname: String, val username: String, val password: String) : RegistrationScreenEvent()
    data class SignUpButtonPressed(val unusedParameter: Unit = Unit) : RegistrationScreenEvent()




}