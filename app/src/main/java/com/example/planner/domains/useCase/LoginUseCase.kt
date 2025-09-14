package com.example.planner.domains.useCase



import com.example.planner.domains.model.ValidationResult
import com.example.planner.data.repository.AuthRepository
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {

    suspend fun performLogin(email: String, password: String): ValidationResult {
        return try {
            val uid = repository.performLogin(email, password)

            if (uid.isNotEmpty()) {
                ValidationResult.Success
            } else {
                ValidationResult.Error("Invalid username or password. Please try again.")
            }
        } catch (e: Exception) {
            ValidationResult.Error(e.message ?: "An error occurred when trying to log in. Please try again.")
        }
    }
}
