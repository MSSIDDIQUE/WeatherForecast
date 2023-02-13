package com.baymax.weather.forecast.weather_forecast.presentation.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.hasLocationPermissions
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.isGpsActive
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.requestLocationPermission
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.turnOnGPS
import com.baymax.weather.forecast.presentation.activities.BaseComposeActivity
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.presentation.screens.HomeScreen
import com.baymax.weather.forecast.weather_forecast.presentation.screens.NavGraphs
import com.baymax.weather.forecast.weather_forecast.presentation.screens.destinations.HomeScreenDestination
import com.baymax.weather.forecast.weather_forecast.presentation.view_model.HomeFragmentViewModel
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.manualcomposablecalls.composable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseComposeActivity() {

    companion object {
        private const val MULTIPLE_LOCATION_PERMISSION = 1
        private const val LOCATION_SETTINGS_REQUEST = 1
        private const val BACK_PRESS_INTERVAL: Long = 3 * 1000
    }

    private val viewModel: HomeFragmentViewModel by viewModels { viewModelFactory }

    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateLocation()
    }

    @Composable
    override fun Content() {
        DestinationsNavHost(navGraph = NavGraphs.root) {
            composable(HomeScreenDestination) {
                HomeScreen(viewModel = viewModel)
            }
        }
    }

    private fun updateLocation() = lifecycleScope.launch {
        withContext(Dispatchers.Default) {
            viewModel.isLastLocationCached()
        }.also { available ->
            if (available) viewModel.updateLastLocation() else updateCurrentDeviceLocation()
        }
    }

    private fun updateCurrentDeviceLocation() {
        when {
            hasLocationPermissions() && isGpsActive() -> viewModel.updateCurrentLocation()
            !hasLocationPermissions() -> requestLocationPermission()
            !isGpsActive() -> turnOnGPS()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        arrayOfPermissiions: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, arrayOfPermissiions)
        if (requestCode == MULTIPLE_LOCATION_PERMISSION) {
            updateCurrentDeviceLocation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_SETTINGS_REQUEST) {
            when (resultCode) {
                Activity.RESULT_OK -> updateCurrentDeviceLocation()
                Activity.RESULT_CANCELED -> viewModel.setSnackBarState(
                    SnackBarViewState.Warning(
                        getString(R.string.gps_warning),
                        "Retry",
                    ) { turnOnGPS() },
                )
            }
        }
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            if (exit) {
                finishAffinity()
            } else {
                viewModel.setSnackBarState(SnackBarViewState.Normal(getString(R.string.backpress_message)))
                exit = true
                delay(BACK_PRESS_INTERVAL)
                exit = false
            }
        }
    }
}
