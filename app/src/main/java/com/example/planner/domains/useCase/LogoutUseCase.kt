package com.example.planner.domains.useCase

import com.example.planner.data.repository.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend fun logoutUser(){
          repository.logout()
    }

}