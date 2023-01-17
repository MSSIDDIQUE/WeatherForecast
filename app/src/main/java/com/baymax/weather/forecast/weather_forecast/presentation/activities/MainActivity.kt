package com.baymax.weather.forecast.weather_forecast.presentation.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.databinding.ActivityMainBinding
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.hasLocationPermissions
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.isGpsActive
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.requestLocationPermission
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.turnOnGPS
import com.baymax.weather.forecast.presentation.activities.BaseBindingActivity
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.utils.ConnectionLiveData
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeFragmentViewModel
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
    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = getViewModelInstanceWithOwner(this).apply {
            when {
                hasLocationPermissions() && isGpsActive() -> fetchAndUpdateDeviceLocation()
                !hasLocationPermissions() -> requestLocationPermission()
                else -> turnOnGPS()
            }
        }
        setupObservers()
    }

    private fun setupObservers() = with(binding) {
        networkState.observe(this@MainActivity) { isActive ->
            noInternetBackground.apply {
                visibility = if (!isActive) View.VISIBLE else View.GONE
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
                    if (isGpsActive()) {
                        viewModel?.fetchAndUpdateDeviceLocation()
                    } else {
                        turnOnGPS()
                    }
                } else {
                    showSnackBar(SnackBarViewState.Warning(getString(R.string.permission_required_warning)))
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
                    SnackBarViewState.Warning(
                        getString(R.string.gps_warning),
                        "Retry"
                    ) { turnOnGPS() }
                )
            }
        }
    }

    override fun onBackPressed() {
        if (exit) {
            finish() // finish activity
        } else {
            showSnackBar(SnackBarViewState.Normal(getString(R.string.backpress_message)))
            exit = true
            Handler(Looper.getMainLooper()).postDelayed(
                { exit = false },
                BACK_PRESS_INTERVAL
            )
        }
    }
}
