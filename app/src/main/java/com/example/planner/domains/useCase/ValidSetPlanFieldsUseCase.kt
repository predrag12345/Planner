package com.example.planner.domains.useCase


import com.example.planner.presentation.setPlan.SetPlanState
import java.time.LocalDateTime
import javax.inject.Inject

class ValidSetPlanFieldsUseCase @Inject constructor() {

    operator fun invoke(state: SetPlanState): Result<Unit> {
        return when {
            state.title.isBlank() -> Result.failure(IllegalArgumentException("Title is required"))
            state.description.isBlank() -> Result.failure(IllegalArgumentException("Description is required"))
            state.startDate == null -> Result.failure(IllegalArgumentException("Start date is required"))
            state.endDate == null -> Result.failure(IllegalArgumentException("End date is required"))
            state.endDate.isBefore(state.startDate) || state.endDate.isEqual(state.startDate) ->
                Result.failure(IllegalArgumentException("End date must be after start date"))
            else -> Result.success(Unit)
        }
    }
}
