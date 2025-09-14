package com.example.planner.data.localDataSource

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPref @Inject constructor(@ApplicationContext private val context: Context):LocalDataSource {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


    override suspend fun checkIfUserIsLogged(): Boolean {
        return sharedPreferences.contains("token")
    }
}