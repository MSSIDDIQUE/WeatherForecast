package com.baymax.weatherforcast.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.baymax.weatherforcast.R
import com.baymax.weatherforcast.ui.fragments.home_fragment.ui.HomeFragmentViewModel
import com.baymax.weatherforcast.utils.ConnectionLiveData
import com.baymax.weatherforcast.utils.locationFlow
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.properties.Delegates


class MainActivity : DaggerAppCompatActivity() {

    companion object {
        private const val MULTIPLE_LOCATION_PERMISSION = 1
        private const val LOCATION_SETTINGS_REQUEST = 1
        private const val BACK_PRESS_INTERVAL: Long = 3 * 1000
    }

    val permissions = arrayListOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )
    val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var locationClient: FusedLocationProviderClient

    @Inject
    lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var viewModel: HomeFragmentViewModel
    private var isDialogVisible = false
    private var exit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(HomeFragmentViewModel::class.java)
        connectionLiveData.observe(this@MainActivity, {
            no_internet_background.visibility = if (it) View.GONE else View.VISIBLE
        })
    }

    fun startCollectingDeviceLocation() = lifecycleScope.launchWhenStarted {
        if (!no_internet_background.isVisible) {
            locationClient.locationFlow().collect { location ->
                viewModel.getWeather(location)
            }
        } else {
            showSnackbar(getString(R.string.no_internet_connection))
        }
    }

    fun turnOnGPS() {
        if (isDialogVisible) {
            return
        }
        isDialogVisible = true
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
                isDialogVisible = false
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
                        showSnackbar(getString(R.string.unable_to_turn_on_gps))
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    fun showSnackbar(msg: String) {
        Snackbar.make(
            main_layout,
            msg,
            Snackbar.LENGTH_LONG
        ).show()
        progressBar.visibility = View.GONE
    }

    private fun showSnackbarWithAction(
        msg: String,
        actionName: String,
        action: () -> Unit
    ) {
        Snackbar.make(
            main_layout,
            msg,
            Snackbar.LENGTH_LONG
        ).setAction(
            actionName
        ) { action() }.show()
        progressBar.visibility = View.GONE
    }

    fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            MULTIPLE_LOCATION_PERMISSION
        )
    }

    fun hasLocationPermission(): Boolean {
        permissions.forEach { permission ->
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
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
        if (requestCode == MULTIPLE_LOCATION_PERMISSION) {
            var allPermissionsGranted = true
            if (grantResults.isNotEmpty()) {
                grantResults.forEach { permissionResult ->
                    if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false
                    }
                }
                if (allPermissionsGranted) {
                    if (isGPSActive()) {
                        startCollectingDeviceLocation()
                    } else {
                        turnOnGPS()
                    }
                } else {
                    showSnackbar(
                        getString(R.string.permission_required_warning)
                    )
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                startCollectingDeviceLocation()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                showSnackbarWithAction(
                    getString(R.string.gps_warning),
                    "Retry"
                ) { turnOnGPS() }
            }
            isDialogVisible = false
        }
    }

    override fun onBackPressed() {
        if (exit) {
            finish() // finish activity
        } else {
            showSnackbar(getString(R.string.backpress_message))
            exit = true
            Handler(Looper.getMainLooper()).postDelayed(
                Runnable
                { exit = false },
                BACK_PRESS_INTERVAL
            )
        }
    }
}
