package com.example.planner.presentation.dashboard

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
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PlansRepository
) : BaseViewModel<DashboardEvent, DashboardState, DashboardEffect>() {

    override fun setInitialState(): DashboardState = DashboardState()

    override fun handleEvents(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.LoadCalendar -> {
                loadCalendar()
                observePlans()
            }
            is DashboardEvent.DayClicked -> {
                viewModelScope.launch {
                    setEffect { DashboardEffect.NavigateToDayDetail(event.date.toString()) }
                }
            }
        }
    }

    private fun loadCalendar() {
        val currentMonth = YearMonth.now()
        val days = (1..currentMonth.lengthOfMonth()).map { day ->
            LocalDate.of(currentMonth.year, currentMonth.month, day)
        }
        val title =
            "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}"

        setState {
            copy(monthTitle = title, daysInMonth = days, today = LocalDate.now())
        }
    }

    private fun observePlans() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            repository.getPlansForUser(userId).collect { plans ->
                setState { copy(plans = plans) }
            }
        }
    }
}

sealed class DashboardEvent : ViewEvent {
    object LoadCalendar : DashboardEvent()
    data class DayClicked(val date: LocalDate) : DashboardEvent()
}

data class DashboardState(
    val monthTitle: String = "",
    val daysInMonth: List<LocalDate> = emptyList(),
    val today: LocalDate = LocalDate.now(),
    val plans: List<Plan> = emptyList()
) : ViewState

sealed class DashboardEffect : ViewSideEffect {
    data class NavigateToDayDetail(val date: String) : DashboardEffect()
}
