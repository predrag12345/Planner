package com.example.planner.presentation.settings


import androidx.lifecycle.viewModelScope
import com.example.planner.domains.useCase.LogoutUseCase
import com.example.planner.presentation.BaseViewModel
import com.example.planner.presentation.ViewEvent
import com.example.planner.presentation.ViewSideEffect
import com.example.planner.presentation.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase,
) : BaseViewModel<SettingsScreenEvent, SettingsScreenViewState, SettingsScreenEffect>() {

    override fun setInitialState(): SettingsScreenViewState {
        return SettingsScreenViewState(false)
    }

    override fun handleEvents(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.LogoutButtonPressed -> {
                setState { copy(isLogoutDialogVisible = true) }
            }
            is SettingsScreenEvent.CancelButtonPressed -> cancelLogout()

            is SettingsScreenEvent.ConfirmButtonPressed -> confirmLogout()

        }
    }

    private fun confirmLogout() {
        viewModelScope.launch {
            setState { copy(isLogoutDialogVisible = false) }
            loadLoginScreen()
        }
    }

    private fun cancelLogout() {
        setState { copy(isLogoutDialogVisible = false) }
    }

    private fun loadLoginScreen() {
        viewModelScope.launch {
            val navigateToLoginScreen = logoutUseCase.logoutUser()
            setEffect { SettingsScreenEffect.NavigateToLoginScreen }

        }
    }

}



sealed class SettingsScreenEffect : ViewSideEffect {
    object NavigateToLoginScreen : SettingsScreenEffect()
    object HandleError : SettingsScreenEffect()
}

sealed class SettingsScreenEvent : ViewEvent {
    object LogoutButtonPressed : SettingsScreenEvent()
    object CancelButtonPressed : SettingsScreenEvent()
    object ConfirmButtonPressed : SettingsScreenEvent()

}

data class SettingsScreenViewState(val isLogoutDialogVisible: Boolean) : ViewState
