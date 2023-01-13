package com.baymax.weather.forecast.data

sealed class BaseViewState<out T> {
    data class Success<T>(val data: T?) : BaseViewState<T>()
    data class Error<T>(val errorMessage: String) : BaseViewState<T>()
    data class Loading<T>(val msg: String) : BaseViewState<T>()
    object Empty : BaseViewState<Nothing>()
}
