package com.baymax.weather.forecast.presentation.view_models

import androidx.lifecycle.ViewModel
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {

    private val _snackBarViewState = MutableStateFlow<SnackBarViewState>(SnackBarViewState.Nothing)
    val snackBarViewState: StateFlow<SnackBarViewState> = _snackBarViewState

    private val _showProgressBarState = MutableStateFlow(false)
    val showProgressBarState: StateFlow<Boolean> = _showProgressBarState

    fun setSnackBarViewState(viewState: SnackBarViewState) {
        _snackBarViewState.value = viewState
    }

    fun setProgressBarState(progressBarState: Boolean) {
        _showProgressBarState.value = progressBarState
    }
}
