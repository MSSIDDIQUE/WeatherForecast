package com.baymax.weather.forecast.fetch_location.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.fetch_location.api.data_transfer_model.Location
import com.baymax.weather.forecast.utils.Constants
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit

object LocationUtils {

    private const val MULTIPLE_LOCATION_PERMISSION = 1
    private const val LOCATION_SETTINGS_REQUEST = 1

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            TimeUnit.SECONDS.toMillis(Constants.UPDATE_INTERVAL_SECS)
        ).setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(Constants.FASTEST_UPDATE_INTERVAL_SECS))
            .build()
    }

    private val locationSettingsRequest: LocationSettingsRequest by lazy {
        LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
            .setAlwaysShow(true)
            .build()
    }

    @SuppressLint("MissingPermission")
    fun FusedLocationProviderClient.locationFlow(context: Context): Flow<ResponseWrapper<Location>> =
        callbackFlow {
            val callBack = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val location = locationResult.lastLocation
                    val userLocation = Location(
                        lat = location?.latitude ?: 0.0,
                        lng = location?.longitude ?: 0.0
                    )
                    try {
                        this@callbackFlow.trySend(ResponseWrapper.Success(userLocation)).isSuccess
                        removeLocationUpdates(this)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            with(context) {
                when {
                    hasLocationPermissions() && isGpsActive() -> requestLocationUpdates(
                        locationRequest,
                        callBack,
                        Looper.getMainLooper()
                    ).addOnFailureListener { e ->
                        close(e)
                    }

                    !hasLocationPermissions() -> trySend(ResponseWrapper.Failure("Please provide location permission"))
                    else -> trySend(ResponseWrapper.Failure("Please turn on your GPS"))
                }
            }
            awaitClose {
                removeLocationUpdates(callBack)
            }
        }

    fun Context.hasLocationPermissions() = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).any {
        checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED
    }.not()

    fun Context.isGpsActive() = try {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } catch (e: Exception) {
        e.printStackTrace()
        false
    }

    fun AppCompatActivity.requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            MULTIPLE_LOCATION_PERMISSION
        )
    }

    fun AppCompatActivity.turnOnGPS() {
        LocationServices.getSettingsClient(this)
            .checkLocationSettings(locationSettingsRequest).addOnCompleteListener { task ->
                try {
                    task.getResult(ApiException::class.java)
                } catch (ex: ApiException) {
                    when (ex.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvableApiException = ex as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                this,
                                LOCATION_SETTINGS_REQUEST
                            )
                        } catch (_: IntentSender.SendIntentException) {
                        }

                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }
                }
            }
    }
}
