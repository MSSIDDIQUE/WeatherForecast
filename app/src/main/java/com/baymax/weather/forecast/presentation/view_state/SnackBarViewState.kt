package com.baymax.weather.forecast.presentation.view_state

sealed interface SnackBarViewState {
    object Nothing : SnackBarViewState
    data class Success(
        val message: String,
        val actionName: String? = null,
        val action: (() -> Unit)? = null
    ) : SnackBarViewState

    data class Error(
        val errorMessage: String,
        val actionName: String? = null,
        val action: (() -> Unit)? = null
    ) : SnackBarViewState

    data class Warning(
        val warningMessage: String,
        val actionName: String? = null,
        val action: (() -> Unit)? = null
    ) : SnackBarViewState

    data class Normal(
        val message: String,
        val actionName: String? = null,
        val action: (() -> Unit)? = null
    ) : SnackBarViewState
}
