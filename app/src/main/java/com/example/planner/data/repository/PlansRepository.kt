package com.example.planner.data.repository

import kotlinx.coroutines.flow.Flow

interface PlansRepository {

    suspend fun addPlan(userId: String, plan: Plan): Result<String>
    fun getPlansForUser(userId: String): Flow<List<Plan>>
}