package com.baymax.weather.forecast.presentation.view_state

sealed interface ProgressBarViewState {
    object Hide : ProgressBarViewState
    data class Show(val msg: String? = null) : ProgressBarViewState
}
