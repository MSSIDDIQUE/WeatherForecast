package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import java.util.*
import java.util.concurrent.TimeUnit

class LocationProviderUseCase(
    private val appContext: Context,
    private val client:FusedLocationProviderClient
    ) {
    companion object {
        private const val UPDATE_INTERVAL_SECS = 10L
        private const val FASTEST_UPDATE_INTERVAL_SECS = 2L
    }

    fun fetchUpdates():Flow<String> = callbackFlow {
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(UPDATE_INTERVAL_SECS)
            fastestInterval = TimeUnit.SECONDS.toMillis(FASTEST_UPDATE_INTERVAL_SECS)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation
                val userLocation = MapLocation(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    bearing = location.bearing
                )
                try {
                    this@callbackFlow.trySend(getDeviceCityName(userLocation)).isSuccess
                }catch (e:Exception){
                    Log.d("(Saquib)","error in fetching the location ${e.message.toString()}")
                }
            }
        }

        client.requestLocationUpdates(
            locationRequest,
            callBack,
            Looper.getMainLooper()
        ).addOnFailureListener {e->
            close(e)
        }
        awaitClose {
            client.removeLocationUpdates(callBack)
        }
    }

    private fun getDeviceCityName(location:MapLocation):String{
        val geocoder = Geocoder(appContext, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            5)
        val cityName: String = addresses[0].locality
        Log.d("(Saquib)","the city name is "+cityName)
        return cityName
    }
}