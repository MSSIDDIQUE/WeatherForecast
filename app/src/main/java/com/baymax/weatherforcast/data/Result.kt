package com.baymax.weatherforcast.data

/**
 * A generic class that holds a value with its loading status.
 *
 * com.baymax.quotable.api.Result is usually created by the Repository classes where they return
 * `LiveData<com.baymax.quotable.api.Result<T>>` to pass back the latest data to the UI with its fetch status.
 */


sealed class Result<T>(val data: T? = null, msg: String? = null) {
    class Success<T>(data: T) : Result<T>(data)
    class Failure<T>(data: T? = null, msg: String?) : Result<T>(data, msg)
    class Loading<T> : Result<T>()
}