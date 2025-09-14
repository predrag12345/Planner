package com.example.planner.presentation.viewPlan

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.planner.presentation.BaseViewModel
import com.example.planner.presentation.ViewEvent
import com.example.planner.presentation.ViewSideEffect
import com.example.planner.presentation.ViewState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
data class ViewPlanState(
    val title: String = "",
    val description: String = "",
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val notificationEnabled: Boolean = false
) : ViewState

sealed class ViewPlanEvent : ViewEvent {
    data class LoadPlan(
        val title: String,
        val description: String,
        val startDate: String,
        val endDate: String
    ) : ViewPlanEvent()
}

sealed class ViewPlanEffect : ViewSideEffect

@RequiresApi(Build.VERSION_CODES.O)
class ViewPlanViewModel : BaseViewModel<ViewPlanEvent, ViewPlanState, ViewPlanEffect>() {

    override fun setInitialState(): ViewPlanState = ViewPlanState()

    override fun handleEvents(event: ViewPlanEvent) {
        when (event) {
            is ViewPlanEvent.LoadPlan -> {
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                setState {
                    copy(
                        title = event.title,
                        description = event.description,
                        startDate = LocalDateTime.parse(event.startDate, formatter),
                        endDate = LocalDateTime.parse(event.endDate, formatter)
                    )
                }
            }
        }
    }
}
