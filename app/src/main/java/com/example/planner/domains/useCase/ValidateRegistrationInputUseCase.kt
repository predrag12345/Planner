package com.example.planner.domains.useCase

import com.example.planner.domains.model.ValidationResult
import com.example.planner.data.repository.AuthRepository

import javax.inject.Inject

class ValidateRegistrationInputUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend fun valid(firstname: String, lastname: String, email: String, password: String): ValidationResult {
        if (repository.areFieldsEmpty(firstname,lastname,email,password) ) {
            return ValidationResult.Error("Please fill out all fields.")
        } else {
            return if (repository.isEmailValid(email)) {
                ValidationResult.Success
            } else {
                ValidationResult.Error("Please enter a valid email address.")
            }
        }
    }



}