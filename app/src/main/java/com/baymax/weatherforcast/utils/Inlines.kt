package com.baymax.weatherforcast.utils

import android.content.SharedPreferences

inline operator fun <reified T> SharedPreferences.Editor.set(key: String, value: T) {
    when (T::class.java) {
        Int::class.java -> putInt(key, value as Int).apply()
        Long::class.java -> putLong(key, value as Long).apply()
        String::class.java -> putString(key,value as String).apply()
    }
}

inline operator fun <reified T> SharedPreferences.get(key: String, defValue: T): T = when (T::class.java) {
    Int::class.java -> getInt(key, defValue as Int) as T
    Long::class.java -> getLong(key, defValue as Long) as T
    String::class.java -> getString(key,defValue as String) as T
    else -> 0 as T
}