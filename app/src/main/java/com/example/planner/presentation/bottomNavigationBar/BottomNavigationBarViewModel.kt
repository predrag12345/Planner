package com.example.planner.presentation.bottomNavigationBar

import androidx.lifecycle.viewModelScope
import com.example.planner.presentation.BaseViewModel
import com.example.planner.presentation.ViewEvent
import com.example.planner.presentation.ViewSideEffect
import com.example.planner.presentation.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BottomNavigationBarViewModel @Inject constructor() :
    BaseViewModel<BottomNavigationBarEvent, BottomNavigationBarViewState, BottomNavigationBarEffect>() {
    override fun setInitialState(): BottomNavigationBarViewState {
        return BottomNavigationBarViewState.Initial
    }

    override fun handleEvents(event: BottomNavigationBarEvent) {
    }

    fun loadNextScreen(itemName: String) {
        viewModelScope.launch {

            setEffect { BottomNavigationBarEffect.NavigateToNextScreen(itemName) }

        }
    }


}


sealed class BottomNavigationBarEffect : ViewSideEffect {
    data class NavigateToNextScreen(val itemName: String) : BottomNavigationBarEffect()

}

sealed class BottomNavigationBarEvent : ViewEvent {
}

sealed class BottomNavigationBarViewState : ViewState {
    object Initial : BottomNavigationBarViewState()

}