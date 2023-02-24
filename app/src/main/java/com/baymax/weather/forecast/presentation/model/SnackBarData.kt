package com.baymax.weather.forecast.presentation.model

data class SnackBarData(
    val message: String,
    val actionLabel: String? = null,
    val action: (() -> Unit)? = null,
)