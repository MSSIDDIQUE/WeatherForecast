package com.baymax.weather.forecast.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.baymax.weather.forecast.BuildConfig

class PrefHelper(val context: Context) {
    companion object keys {
        const val WEATHER_API_KEY: String = "appid"
        const val LAT: String = "lat"
        const val LNG: String = "lng"
        const val LOCATION_PERMISSION: String = "location_permission"
        const val GOOGLE_PLACE_API_KEY: String = "key"
    }

    val masterKeyAlias: String by lazy {
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }
    val sharedPref: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            "open_weather_forecast_api",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    val putSharedPrefs = sharedPref.edit()

    init {
        putSharedPrefs[LAT] = 0
        putSharedPrefs[LNG] = 0
        putSharedPrefs[LOCATION_PERMISSION] = false
        putSharedPrefs[WEATHER_API_KEY] = BuildConfig.WEATHER_API_KEY
        putSharedPrefs[GOOGLE_PLACE_API_KEY] = BuildConfig.GOOGLE_PLACE_API_KEY
    }
}
