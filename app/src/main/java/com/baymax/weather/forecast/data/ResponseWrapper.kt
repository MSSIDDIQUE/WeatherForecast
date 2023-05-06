package com.baymax.weather.forecast.data

import androidx.annotation.Keep

@Keep
sealed class ResponseWrapper<T> {
    data class Success<T>(val data: T) : ResponseWrapper<T>()
    data class Failure<T>(val msg: String?) : ResponseWrapper<T>()
    object Empty: ResponseWrapper<Nothing>()
}
