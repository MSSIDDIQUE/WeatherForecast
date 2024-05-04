package com.baymax.weather.forecast.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.baymax.weather.forecast.data.ApiResponse
import com.baymax.weather.forecast.data.HttpExceptions
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import kotlinx.serialization.SerializationException

operator fun SharedPreferences.set(key: String, value: Any?) = when (value) {
    is String? -> edit { putString(key, value) }
    is Int -> edit { putInt(key, value) }
    is Boolean -> edit { putBoolean(key, value) }
    is Float -> edit { putFloat(key, value) }
    is Long -> edit { putLong(key, value) }
    else -> throw UnsupportedOperationException("Not yet implemented")
}

inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null,
): T = when (T::class) {
    String::class -> getString(key, defaultValue as? String ?: "") as T
    Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
    Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
    Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
    Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
    else -> throw UnsupportedOperationException("Not yet implemented")
}

suspend inline fun <reified T, reified E> HttpClient.safeRequest(
    block: HttpRequestBuilder.() -> Unit,
): ApiResponse<T, E> =
    try {
        val response = request { block() }
        ApiResponse.Success(response.body())
    } catch (exception: ClientRequestException) {
        ApiResponse.Error.HttpError(
            code = exception.response.status.value,
            errorBody = exception.response.body(),
            errorMessage = "Status Code: ${exception.response.status.value} - API Key Missing",
        )
    } catch (exception: HttpExceptions) {
        ApiResponse.Error.HttpError(
            code = exception.response.status.value,
            errorBody = exception.response.body(),
            errorMessage = exception.message,
        )
    } catch (e: SerializationException) {
        ApiResponse.Error.SerializationError(errorMessage = e.message)
    } catch (e: Exception) {
        ApiResponse.Error.GenericError(errorMessage = e.message)
    }
