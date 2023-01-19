package com.baymax.weather.forecast.presentation.listeners

import com.baymax.weather.forecast.presentation.view_state.ProgressBarViewState
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState

interface BaseEventListener {
    fun updateProgressBarState(viewState: ProgressBarViewState)
    fun showSnackBar(viewState: SnackBarViewState)
}
