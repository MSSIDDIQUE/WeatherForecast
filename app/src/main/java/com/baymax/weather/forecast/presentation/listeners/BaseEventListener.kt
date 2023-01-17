package com.baymax.weather.forecast.presentation.listeners

import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState

interface BaseEventListener {
    fun showProgressBar(isVisible: Boolean, message: String? = null)
    fun showSnackBar(viewState: SnackBarViewState)
}
