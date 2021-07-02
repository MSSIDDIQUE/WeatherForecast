package com.baymax.weatherforcast.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModel
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFramentViewModelFactory
import com.baymax.weatherforcast.ui.fragments.splash_screen_fragment.SplashScreenFragmentDirections
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), KodeinAware {

    companion object {
        private const val MULTIPLE_LOCAITON_PERMISSION = 1
        private const val LOCATION_SETTINGS_REQUEST = 1
        private const val SPLASH_TIME_OUT: Long = 4000
    }

    val permissions = arrayListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }
    private val viewModelFactory: HomeFramentViewModelFactory by instance()
    private lateinit var viewModel: HomeFramentViewModel
    override val kodein by kodein()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeFramentViewModel::class.java)
        val permissions_granted = hasLocationPermission()
        val gps_active = isGPSActive()
        if (permissions_granted && gps_active) {
            viewModel.setGpsStatus(true)
        }
        if (!permissions_granted) {
            requestLocationPermission()
        } else if (!gps_active) {
            turnOnGPS()
        }
        viewModel.setNetworkAvailable(isOnline(this))
        viewModel.setGpsStatus(isGPSActive())
        viewModel.readyToFetch.observe(this, { (gpsStatus, networkStatus) ->
            if (gpsStatus != null && networkStatus != null) {
                if (networkStatus == false) {
                    val direction =
                        SplashScreenFragmentDirections.actionToErrorFragment(
                            "No internet connection"
                        )
                    navController.navigate(direction)
                    Snackbar.make(
                        main_layout,
                        "No Internet Connection",
                        Snackbar.LENGTH_LONG
                    ).show()
                } else if (gpsStatus == false) {
                    Snackbar.make(
                        main_layout,
                        "Turn on your GPS",
                        Snackbar.LENGTH_LONG
                    ).setAction(
                        "Retry"
                    ) {
                        turnOnGPS()
                    }.show()
                } else if (gpsStatus == true && networkStatus == true) {
                    val direction =
                        SplashScreenFragmentDirections.actionToHomeFragment()
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate(direction)
                    }, SPLASH_TIME_OUT)
                }
            }
        })
    }


    fun turnOnGPS() {
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000.toLong())
            .setFastestInterval(1 * 1000.toLong())
        val settingsBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)
        val result =
            LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build())

        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
            } catch (ex: ApiException) {
                when (ex.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException =
                            ex as ResolvableApiException
                        resolvableApiException
                            .startResolutionForResult(
                                this,
                                LOCATION_SETTINGS_REQUEST
                            )
                    } catch (e: IntentSender.SendIntentException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            MULTIPLE_LOCAITON_PERMISSION
        )
    }

    fun hasLocationPermission(): Boolean {
        permissions.forEach { permisson ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permisson
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MULTIPLE_LOCAITON_PERMISSION) {
            var allPermissionsGranted = true
            if (grantResults.isNotEmpty()) {
                grantResults.forEach { permissionResult ->
                    if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                    }
                }
                if (allPermissionsGranted) {
                    if (!isGPSActive()) {
                        turnOnGPS()
                    } else {
                        viewModel.setGpsStatus(true)
                    }
                } else {
                    Snackbar.make(
                        main_layout,
                        "Provide all the permissions",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun isGPSActive(): Boolean {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var gpsEnabled by Delegates.notNull<Boolean>()
        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            return false
        }
        return gpsEnabled
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.setGpsStatus(true)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Snackbar.make(
                    main_layout,
                    "Please turn on you GPS!",
                    Snackbar.LENGTH_LONG
                ).setAction(
                    "Retry"
                ) {
                    turnOnGPS()
                }.show()
                viewModel.setGpsStatus(false)
            }
        }
    }
}
