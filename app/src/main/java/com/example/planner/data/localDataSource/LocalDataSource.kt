package com.example.planner.data.localDataSource

interface LocalDataSource {

    suspend fun checkIfUserIsLogged(): Boolean

}