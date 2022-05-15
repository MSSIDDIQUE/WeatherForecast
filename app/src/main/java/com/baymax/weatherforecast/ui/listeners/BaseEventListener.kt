package com.baymax.weatherforecast.ui.listeners

interface BaseEventListener {
    fun showProgressBar(isVisible: Boolean, message: String? = null)
    fun showSnackBar(message: String, actionName: String? = null, action: (() -> Unit)? = null)
}