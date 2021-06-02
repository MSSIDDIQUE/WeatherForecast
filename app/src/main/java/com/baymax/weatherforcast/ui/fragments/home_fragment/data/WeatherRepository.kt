package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import androidx.lifecycle.LiveData
import com.baymax.weatherforcast.api.response.WeatherResponse
import com.baymax.weatherforcast.data.Result
import com.baymax.weatherforcast.utils.PrefHelper
import com.baymax.weatherforcast.utils.get
import com.baymax.weatherforcast.utils.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val weatherDao: WeatherDataDao,
    private val remoteDataSource: WeatherRemoteDataSource,
    private val prefHelper: PrefHelper
)  {
    init {
    }
    suspend fun getWeather(): LiveData<List<WeatherData>> {
        return withContext(Dispatchers.IO){
            weatherDao.getAllRecords()
        }
    }

    suspend fun getWeatherOfCity(
        city:String
    ):Result<WeatherResponse>{
        var saved_location = "London"
        saved_location = prefHelper.sharedPref[PrefHelper.LOCATION,"London"]
        if(!saved_location.equals(city)){
            prefHelper.sharedPref.edit()[PrefHelper.LOCATION] = city
            return remoteDataSource.fetchWeatherOfCity(city)
        }
        return remoteDataSource.fetchWeatherOfCity(saved_location)
    }

}