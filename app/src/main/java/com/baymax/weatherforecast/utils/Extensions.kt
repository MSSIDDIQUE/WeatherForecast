package com.baymax.weatherforecast.utils

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.baymax.weatherforecast.api.response.googlePlaceApi.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

suspend fun FusedLocationProviderClient.locationFlow(): Flow<Location> = callbackFlow {
    val locationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(Constants.UPDATE_INTERVAL_SECS)
        fastestInterval = TimeUnit.SECONDS.toMillis(Constants.FASTEST_UPDATE_INTERVAL_SECS)
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }
    val callBack = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            val userLocation = Location(
                lat = location.latitude,
                lng = location.longitude
            )
            try {
                this@callbackFlow.trySend(userLocation).isSuccess
                removeLocationUpdates(this)
            } catch (e: Exception) {
            }
        }
    }

    requestLocationUpdates(
        locationRequest,
        callBack,
        Looper.getMainLooper()
    ).addOnFailureListener { e ->
        close(e)
    }
    awaitClose {
        removeLocationUpdates(callBack)
    }
}