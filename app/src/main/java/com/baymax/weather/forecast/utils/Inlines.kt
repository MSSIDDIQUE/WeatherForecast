package com.baymax.weather.forecast.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.baymax.weather.forecast.data.ResponseWrapper

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

inline fun <reified T, reified R> ResponseWrapper<T>.map(transform: (T) -> R): ResponseWrapper<R> {
    return when (this) {
        is ResponseWrapper.Success -> ResponseWrapper.Success(transform(data))
        is ResponseWrapper.Failure -> ResponseWrapper.Failure(msg)
    }
}
