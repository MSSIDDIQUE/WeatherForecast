package com.baymax.weather.forecast.weather_forecast.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.baymax.weather.forecast.R
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.hasLocationPermissions
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.isGpsActive
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.requestLocationPermission
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.turnOnGPS
import com.baymax.weather.forecast.presentation.model.SnackBarData
import com.baymax.weather.forecast.presentation.view_state.SnackBarViewState
import com.baymax.weather.forecast.weather_forecast.presentation.navigation.AppNavigation
import com.baymax.weather.forecast.weather_forecast.presentation.viewmodel.HomeScreenViewModel
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    companion object {
        private const val MULTIPLE_LOCATION_PERMISSION = 1
        private const val LOCATION_SETTINGS_REQUEST = 1
        private const val BACK_PRESS_INTERVAL: Long = 3 * 1000
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: HomeScreenViewModel by viewModels { viewModelFactory }

    private var exit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent { AppNavigation(viewModel) }
        updateLocation()
    }

    private fun updateLocation() = lifecycleScope.launch {
        withContext(Dispatchers.Default) {
            viewModel.isLastLocationCached()
        }.also { available ->
            if (available) viewModel.updateCachedLocation() else updateCurrentDeviceLocation()
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
                RESULT_OK -> updateCurrentDeviceLocation()
                RESULT_CANCELED -> viewModel.setSnackBarState(
                    SnackBarViewState.Show(
                        SnackBarData(
                            getString(R.string.gps_warning),
                            "Retry",
                        ) { turnOnGPS() },
                    ),
                )
            }
        }
    }

    override fun onBackPressed() {
        lifecycleScope.launch {
            if (exit) {
                finishAffinity()
            } else {
                viewModel.setSnackBarState(
                    SnackBarViewState.Show(
                        SnackBarData(
                            getString(R.string.backpress_message),
                        ),
                    ),
                )
                exit = true
                delay(BACK_PRESS_INTERVAL)
                exit = false
            }
        }
    }
}
