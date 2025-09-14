package com.example.planner.data.repository

interface AuthRepository {
    suspend fun userIsLogged(): Boolean
    suspend fun  saveUserData(token:String)
    suspend fun performLogin(email: String, password: String):String
    suspend fun performRegister(firstname: String, lastname: String, email: String, password: String): String
    suspend fun logout()
    suspend fun isEmailValid(email:String):Boolean
    suspend fun areFieldsEmpty(firstname: String, lastname: String, email: String, password: String):Boolean

}