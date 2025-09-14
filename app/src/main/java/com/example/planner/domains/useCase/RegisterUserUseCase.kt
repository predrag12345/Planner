package com.example.planner.domains.useCase


import com.example.planner.domains.model.ValidationResult
import com.example.planner.data.repository.AuthRepository
import com.example.planner.domains.useCase.LoginUseCase
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(private val repository: AuthRepository, private val saveUserTokenUseCase: SaveUserTokenUseCase, private val loginUseCase: LoginUseCase) {


    suspend fun performRegister(
        firstname: String,
        lastname: String,
        email: String,
        password: String
    ): ValidationResult {
        return try {
            val uid = repository.performRegister(firstname, lastname, email, password)

            if (uid.isNotEmpty()) {
                ValidationResult.Success
            } else {
                ValidationResult.Error("An error occurred when trying to register. Please try again.")
            }
        } catch (e: Exception) {
            ValidationResult.Error(e.message ?: "An error occurred when trying to register. Please try again.")
        }
    }


}