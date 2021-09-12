package com.baymax.weatherforcast.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.baymax.weatherforcast.BuildConfig


class PrefHelper(val context:Context) {
    companion object keys{
        const val API_KEY :String = "appid"
        const val LOCATION : String = "current_location"
        const val LOCATION_PERMISSION: String = "location_permission"
    }
    val masterKeyAlias:String by lazy {
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    }
    val sharedPref:SharedPreferences by lazy {
        EncryptedSharedPreferences.create(
            "openweather_forecase_api",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
    init {
        sharedPref.edit()[API_KEY] = BuildConfig.WEATHER_API_KEY
        sharedPref.edit()[LOCATION] = "London"
        sharedPref.edit()[LOCATION_PERMISSION] = false
    }
}