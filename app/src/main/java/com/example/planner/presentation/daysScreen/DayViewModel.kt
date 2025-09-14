package com.example.planner.presentation.daysScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.planner.data.repository.Plan
import com.example.planner.data.repository.PlansRepository
import com.example.planner.presentation.BaseViewModel
import com.example.planner.presentation.ViewEvent
import com.example.planner.presentation.ViewSideEffect
import com.example.planner.presentation.ViewState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject



data class DayViewState(
    val date: LocalDate = LocalDate.now(),
    val dayTitle: String = "",
    val hours: List<String> = (0..23).map { String.format("%02d:00", it) },
    val plans: List<Plan> = emptyList()
) : ViewState

sealed class DayEvent : ViewEvent {
    data class LoadDay(val date: LocalDate) : DayEvent()
}

sealed class DayEffect : ViewSideEffect

@HiltViewModel
class DayViewModel @Inject constructor(
    private val repository: PlansRepository
) : BaseViewModel<DayEvent, DayViewState, DayEffect>() {

    override fun setInitialState(): DayViewState = DayViewState()

    override fun handleEvents(event: DayEvent) {
        when (event) {
            is DayEvent.LoadDay -> {
                loadDay(event.date)
                observePlans(event.date)
            }
        }
    }

    private fun observePlans(date: LocalDate) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            repository.getPlansForUser(userId).collect { plans ->
                val filteredPlans = plans.filter { plan ->
                    val startDate = LocalDate.parse(plan.startDate.substring(0, 10))
                    startDate == date
                }
                setState { copy(plans = filteredPlans) }
            }
        }
    }

    private fun loadDay(date: LocalDate) {
        val dayName = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val monthName = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
        val title = "$dayName, ${date.dayOfMonth} $monthName ${date.year}"

        val hours = (0..23).map { hour ->
            String.format("%02d:00", hour)
        }

        setState {
            copy(
                date = date,
                dayTitle = title,
                hours = hours
            )
        }
    }
}
