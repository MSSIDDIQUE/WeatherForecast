package com.baymax.weather.forecast.weather_forecast.presentation.activities

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.data.BaseViewState
import com.baymax.weather.forecast.databinding.ActivityMainBinding
import com.baymax.weather.forecast.presentation.activities.BaseBindingActivity
import com.baymax.weather.forecast.utils.ConnectionLiveData
import com.baymax.weather.forecast.utils.isGPSActive
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeFragmentViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import javax.inject.Inject

class MainActivity : BaseBindingActivity<ActivityMainBinding, HomeFragmentViewModel>(
    ActivityMainBinding::inflate
) {

    companion object {
        private const val MULTIPLE_LOCATION_PERMISSION = 1
        private const val LOCATION_SETTINGS_REQUEST = 1
        private const val BACK_PRESS_INTERVAL: Long = 3 * 1000
    }

    @Inject
    lateinit var networkState: ConnectionLiveData

    private var viewModel: HomeFragmentViewModel? = null
    private var isDialogVisible = false
    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = getViewModelInstanceWithOwner(this)
        networkState.observe(this) { isActive ->
            binding.noInternetBackground.apply {
                visibility = if (!isActive) View.VISIBLE else View.GONE
            }
        }
    }

    fun turnOnGPS() {
        viewModel?.setUiState(BaseViewState.Loading(getString(R.string.turning_on_gps)))
        if (isDialogVisible) {
            return
        }
        isDialogVisible = true
        val mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(10 * 1000.toLong())
            .setFastestInterval(1 * 1000.toLong())
        val settingsBuilder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest)
        settingsBuilder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this)
            .checkLocationSettings(settingsBuilder.build())

        result.addOnCompleteListener { task ->
            try {
                task.getResult(ApiException::class.java)
                isDialogVisible = false
            } catch (ex: ApiException) {
                when (ex.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = ex as ResolvableApiException
                        resolvableApiException.startResolutionForResult(
                            this,
                            LOCATION_SETTINGS_REQUEST
                        )
                        viewModel?.setUiState(BaseViewState.Loading(getString(R.string.turning_on_gps)))
                    } catch (e: IntentSender.SendIntentException) {
                        showSnackBar(getString(R.string.unable_to_turn_on_gps))
                        viewModel?.setUiState(BaseViewState.Empty)
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        viewModel?.setUiState(BaseViewState.Empty)
                    }
                }
            }
        }
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
                        viewModel?.fetchAndUpdateDeviceLocation()
                    } else {
                        turnOnGPS()
                    }
                } else {
                    showSnackBar(getString(R.string.permission_required_warning))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel?.fetchAndUpdateDeviceLocation()
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                showSnackBar(
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
            showSnackBar(getString(R.string.backpress_message))
            exit = true
            Handler(Looper.getMainLooper()).postDelayed(
                { exit = false },
                BACK_PRESS_INTERVAL
            )
        }
    }
}
