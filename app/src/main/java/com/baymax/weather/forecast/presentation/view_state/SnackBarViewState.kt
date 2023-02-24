package com.baymax.weather.forecast.presentation.view_state

import com.baymax.weather.forecast.presentation.model.SnackBarData

sealed interface SnackBarViewState {
    object Hidden : SnackBarViewState
    data class Show(val snackBarData: SnackBarData) : SnackBarViewState
}
