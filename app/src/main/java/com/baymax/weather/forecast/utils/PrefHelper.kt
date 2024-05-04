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
        const val LNG: String = "lon"
        const val GOOGLE_PLACE_API_KEY: String = "key"
        const val IS_LAST_LOCATION_CACHED: String = "is_last_location_cached"
    }

    private val masterKeyAlias: String by lazy {
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }
    val sharedPrefs: SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            "open_weather_forecast_api",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    init {
        sharedPrefs[WEATHER_API_KEY] = BuildConfig.WEATHER_API_KEY
        sharedPrefs[GOOGLE_PLACE_API_KEY] = BuildConfig.GOOGLE_PLACE_API_KEY
    }
}
