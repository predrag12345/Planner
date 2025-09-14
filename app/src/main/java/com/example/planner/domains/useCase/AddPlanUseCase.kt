package com.example.planner.domains.useCase


import com.example.planner.data.repository.Plan
import com.example.planner.data.repository.PlansRepository
import javax.inject.Inject

class AddPlanUseCase @Inject constructor(
    private val repository: PlansRepository
) {
    suspend operator fun invoke(userId: String, plan: Plan): Result<String> {
        return repository.addPlan(userId, plan)
    }
}
