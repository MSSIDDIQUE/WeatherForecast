package com.baymax.weatherforcast.Utils.Providers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.baymax.weatherforcast.Model.DB.Entity.RecordDb
import com.baymax.weatherforcast.Model.DB.WeatherData
import com.baymax.weatherforcast.Utils.Exceptions.LocationPermissionNotGrantedException
import com.baymax.weatherforcast.Utils.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred
import java.util.*


const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"

class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    context: Context
) : PreferenceProvider(context), LocationProvider {

    private val appContext = context.applicationContext

    override suspend fun hasLocationChanged(lastWeatherLocation: List<WeatherData>): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }

        return deviceLocationChanged
    }

    override suspend fun getPreferredLocationString(): String {

        if (isUsingDeviceLocation()) {
            try {
                val deviceLocation = getLastDeviceLocation().await()
                    ?: return getCustomLocationName()
                Log.d("(Saquib)", "The device lat, lon are "+deviceLocation.latitude+" "+deviceLocation.longitude)
                val location = getDeviceCityName(deviceLocation.latitude,deviceLocation.longitude)
                return location
            } catch (e: LocationPermissionNotGrantedException) {
                return getCustomLocationName()
            }
        }
        else
            return getCustomLocationName()
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: List<WeatherData>): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
            ?: return false

        if(getDeviceCityName(deviceLocation.latitude,deviceLocation.latitude).equals(lastWeatherLocation.get(0).name))
            Log.d("(Saquib)","The device location changed is false")
            return false
        Log.d("(Saquib)","The device location changed is true")
        return true;
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun getCustomLocationName(): String {
        return "Delhi"
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun getDeviceCityName(lat:Double,lon:Double):String{
        val geocoder = Geocoder(appContext, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(lat, lon, 5)
        val cityName: String = addresses[0].locality
        Log.d("(Saquib)","the city name is "+cityName)
        return cityName
    }
}