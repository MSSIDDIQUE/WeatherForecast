package com.baymax.weather.forecast.data

sealed class ResponseWrapper<T> {
    data class Success<T>(val data: T) : ResponseWrapper<T>()
    data class Failure<T>(val msg: String?) : ResponseWrapper<T>()
}
