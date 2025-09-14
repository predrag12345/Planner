package com.example.planner.presentation.weeksScreen

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
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import javax.inject.Inject

sealed class WeekEvent: ViewEvent{
    object LoadWeek : WeekEvent()
}

data class WeekState(
    val daysOfWeek: List<String> = listOf("M", "T", "W", "T", "F", "S", "S"),
    val datesOfWeek: List<LocalDate> = emptyList(),
    val hours: List<String> = (0..23).map { String.format("%02d:00", it) },
    val plans: List<Plan> = emptyList()
) : ViewState


sealed class WeekEffect: ViewSideEffect
@HiltViewModel
class WeekViewModel @Inject constructor(
    private val repository: PlansRepository
) : BaseViewModel<WeekEvent, WeekState, WeekEffect>() {

    override fun setInitialState(): WeekState = WeekState()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun handleEvents(event: WeekEvent) {
        when (event) {
            WeekEvent.LoadWeek -> {
                loadCurrentWeek()
                observePlans()
            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadCurrentWeek() {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        val weekDates = (0..6).map { monday.plusDays(it.toLong()) }

        setState { copy(datesOfWeek = weekDates) }
    }
}


