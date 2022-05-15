package com.baymax.weatherforecast.data

sealed class Result<T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure<T>(val msg: String?) : Result<T>()
}