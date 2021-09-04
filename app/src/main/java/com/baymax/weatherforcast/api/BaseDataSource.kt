package com.baymax.weatherforcast.api

import android.util.Log
import com.baymax.weatherforcast.data.Result
import retrofit2.Response

/**
 * Abstract Base Data source class with error handling
 */
abstract class BaseDataSource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Result.Success(body)
            }
            return Result.Failure(msg = response.code().toString()+" "+response.message().toString())
        } catch (e: Exception) {
            return Result.Failure(msg = e.message.toString())
        }
    }
}

