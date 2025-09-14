package com.example.planner.domains.useCase

import com.example.planner.data.repository.AuthRepository
import javax.inject.Inject

class CheckIfUserIsLoggedUseCase  @Inject constructor(private val repository: AuthRepository){

    suspend fun execute(): Boolean {
        return repository.userIsLogged()
    }

}