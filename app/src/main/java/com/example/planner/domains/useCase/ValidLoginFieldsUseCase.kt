package com.example.planner.domains.useCase

import com.example.planner.domains.model.ValidationResult
import com.example.planner.data.repository.AuthRepository

import javax.inject.Inject

class ValidLoginFieldsUseCase @Inject constructor(private val repository: AuthRepository) {

    fun authenticate(email: String, password: String): ValidationResult {
        if (email.isEmpty() || password.isEmpty()) {
            return ValidationResult.Error("Please fill out both email and password fields.")
        } else {
            fun isEmailValid(email: String): Boolean {
                val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$")
                return emailRegex.matches(email)
            }

            return if (isEmailValid(email)) {
                ValidationResult.Success
            } else {
                ValidationResult.Error("Please enter a valid email address.")
            }
        }
    }
}