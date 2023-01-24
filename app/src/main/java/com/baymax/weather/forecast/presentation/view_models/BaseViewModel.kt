package com.baymax.weather.forecast.presentation.view_models

import androidx.lifecycle.ViewModel
import com.baymax.weather.forecast.presentation.view_state.ProgressBarViewState
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel : ViewModel() {

    private val _snackBar = MutableStateFlow<SnackBarViewState>(SnackBarViewState.Nothing)
    val snackBar: StateFlow<SnackBarViewState> = _snackBar.asStateFlow()

    private val _progressBar = MutableStateFlow<ProgressBarViewState>(ProgressBarViewState.Hide)
    val progressBar: StateFlow<ProgressBarViewState> = _progressBar.asStateFlow()

    fun setSnackBarState(viewState: SnackBarViewState) {
        _snackBar.value = viewState
    }

    fun setProgressBarState(viewState: ProgressBarViewState) {
        _progressBar.value = viewState
    }
}
