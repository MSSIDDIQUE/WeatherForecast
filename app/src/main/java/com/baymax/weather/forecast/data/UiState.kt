package com.baymax.weather.forecast.data

sealed class UiState<out T> {
    data class Success<T>(val data: T?) : UiState<T>()
    data class Error<T>(val errorMessage: String) : UiState<T>()
    data class Loading<T>(val msg: String) : UiState<T>()
    object Empty : UiState<Nothing>()
}
