package com.example.planner.domains.useCase

import com.example.planner.data.repository.AuthRepository
import javax.inject.Inject

class SaveUserTokenUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun writeToken(token:String) {
        repository.saveUserData(token)
    }
}