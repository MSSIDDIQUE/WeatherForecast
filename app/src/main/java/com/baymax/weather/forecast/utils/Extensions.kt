package com.baymax.weather.forecast.utils

import android.content.Context
import android.location.LocationManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.Exception

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun String.toStandardString(): String {
    return try {
        "${this.first().uppercase()}${this.subSequence(1, this.length)}"
    }catch (e: IndexOutOfBoundsException){
        this
    }
}

fun Context.isGPSActive(): Boolean {
    return try {
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }
}