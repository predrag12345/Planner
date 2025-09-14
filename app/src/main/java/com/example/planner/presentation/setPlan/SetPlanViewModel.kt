package com.example.planner.presentation.setPlan

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.planner.data.repository.Plan
import com.example.planner.domains.useCase.AddPlanUseCase
import com.example.planner.domains.useCase.ValidSetPlanFieldsUseCase
import com.example.planner.presentation.BaseViewModel
import com.example.planner.presentation.ViewEvent
import com.example.planner.presentation.ViewSideEffect
import com.example.planner.presentation.ViewState
import com.example.planner.presentation.notifications.ReminderReceiver
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
data class SetPlanState(
    val title: String = "",
    val description: String = "",
    val startDate: LocalDateTime? = null,
    val endDate: LocalDateTime? = null,
    val notificationEnabled: Boolean = false
) : ViewState

sealed class SetPlanEvent : ViewEvent {
    data class TitleChanged(val title: String) : SetPlanEvent()
    data class DescriptionChanged(val description: String) : SetPlanEvent()
    data class StartDateChanged(val dateTime: LocalDateTime) : SetPlanEvent()
    data class EndDateChanged(val dateTime: LocalDateTime) : SetPlanEvent()
    data class NotificationToggled(val enabled: Boolean) : SetPlanEvent()
    object SavePlan : SetPlanEvent()
}

sealed class SetPlanEffect : ViewSideEffect {
    object PlanSaved : SetPlanEffect()
    object PlanSaveError : SetPlanEffect()
    data class ValidationError(val message: String) : SetPlanEffect()
}

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class SetPlanViewModel @Inject constructor(
    private val addPlanUseCase: AddPlanUseCase,
    private val validSetPlanFieldsUseCase: ValidSetPlanFieldsUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel<SetPlanEvent, SetPlanState, SetPlanEffect>() {

    override fun setInitialState(): SetPlanState = SetPlanState()

    override fun handleEvents(event: SetPlanEvent) {
        when (event) {
            is SetPlanEvent.TitleChanged -> setState { copy(title = event.title) }
            is SetPlanEvent.DescriptionChanged -> setState { copy(description = event.description) }
            is SetPlanEvent.StartDateChanged -> setState { copy(startDate = event.dateTime) }
            is SetPlanEvent.EndDateChanged -> setState { copy(endDate = event.dateTime) }
            is SetPlanEvent.NotificationToggled -> setState { copy(notificationEnabled = event.enabled) }
            is SetPlanEvent.SavePlan -> savePlan()
        }
    }

    private fun savePlan() {
        val state = viewState.value

        val validationResult = validSetPlanFieldsUseCase(state)
        if (validationResult.isFailure) {
            val message = validationResult.exceptionOrNull()?.message ?: "Invalid fields"
            setEffect { SetPlanEffect.ValidationError(message) }
            return
        }

        viewModelScope.launch {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

            val plan = Plan(
                title = state.title,
                description = state.description,
                startDate = state.startDate!!.format(formatter),
                endDate = state.endDate!!.format(formatter),
                notificationEnabled = state.notificationEnabled
            )

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val result = addPlanUseCase(userId, plan)
                if (result.isSuccess) {
                    if (state.notificationEnabled) {
                        scheduleReminder(plan.title, plan.description, state.startDate!!)
                    }
                    setEffect { SetPlanEffect.PlanSaved }
                } else {
                    setEffect { SetPlanEffect.PlanSaveError }
                }
            } else {
                setEffect { SetPlanEffect.PlanSaveError }
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun scheduleReminder(title: String, description: String, dateTime: LocalDateTime) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAtMillis = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Ako nema pravo, otvori Settings da user ručno dozvoli
                val settingsIntent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                settingsIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(settingsIntent)
                return
            }
        }

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("title", title)
            putExtra("description", description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            dateTime.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }

}
