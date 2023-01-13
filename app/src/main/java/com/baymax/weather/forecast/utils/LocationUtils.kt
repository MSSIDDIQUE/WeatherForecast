package com.baymax.weather.forecast.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

object LocationUtils {
    suspend fun FusedLocationProviderClient.locationFlow(context: Context): Flow<Location> =
        callbackFlow {
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
                        lat = location?.latitude ?: 0.0,
                        lng = location?.longitude ?: 0.0
                    )
                    try {
                        this@callbackFlow.trySend(userLocation).isSuccess
                        removeLocationUpdates(this)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@callbackFlow
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
}
