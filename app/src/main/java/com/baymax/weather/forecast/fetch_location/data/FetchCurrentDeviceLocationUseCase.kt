package com.baymax.weather.forecast.fetch_location.data

import android.app.Application
import com.baymax.weather.forecast.fetch_location.utils.LocationUtils.locationFlow
import com.google.android.gms.location.FusedLocationProviderClient
import javax.inject.Inject

class FetchCurrentDeviceLocationUseCase @Inject constructor(
    private val appContext: Application,
    private val locationProvider: FusedLocationProviderClient
) {
    operator fun invoke() = locationProvider.locationFlow(appContext)
}
