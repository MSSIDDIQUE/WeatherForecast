package com.baymax.weather.forecast.usecases.weather_forecast.data

import com.baymax.weather.forecast.data.BaseDataSource
import com.baymax.weather.forecast.usecases.search_location.data.data_transfer_model.Location
import com.baymax.weather.forecast.usecases.weather_forecast.api.WeatherApiService
import com.baymax.weather.forecast.utils.PrefHelper
import com.baymax.weather.forecast.utils.get
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApiService: WeatherApiService,
    private val prefHelper: PrefHelper
) : BaseDataSource() {

    suspend fun fetchWeatherForLocation(
        location: Location
    ) = getResult {
        weatherApiService.getWeatherOfCity(
            location.lat.toString(),
            location.lng.toString(),
            prefHelper.sharedPref[PrefHelper.WEATHER_API_KEY, ""]
        )
    }
}
