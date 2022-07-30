package com.baymax.weather.forecast.utils.providers

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

abstract class PreferenceProvider(context: Context) {
    private val appContext = context.applicationContext

    protected val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)
}