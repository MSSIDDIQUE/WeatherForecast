package com.baymax.weatherforcast.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys


class PrefHelper(val context:Context) {
    companion object keys{
        val API_KEY :String = "api_key"
        val LOCATION : String = "current_location"
        val LOCATION_PERMISSION: String = "location_permission"
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
        sharedPref.edit()[API_KEY] = "5f3f95dcfb462c164ddfce910fffe503"
        sharedPref.edit()[LOCATION] = "London"
        sharedPref.edit()[LOCATION_PERMISSION] = false
    }
}