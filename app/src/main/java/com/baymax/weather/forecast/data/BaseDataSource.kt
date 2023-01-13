package com.baymax.weather.forecast.data

import retrofit2.Response

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): ResponseWrapper<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return ResponseWrapper.Success(body)
            }
            return ResponseWrapper.Failure(
                msg = response.code().toString() + " " + response.message().toString()
            )
        } catch (e: Exception) {
            return ResponseWrapper.Failure(msg = e.message.toString())
        }
    }
}

