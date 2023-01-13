package com.baymax.weather.forecast.presentation.listeners

interface BaseEventListener {
    fun showProgressBar(isVisible: Boolean, message: String? = null)
    fun showSnackBar(message: String, actionName: String? = null, action: (() -> Unit)? = null)
}
