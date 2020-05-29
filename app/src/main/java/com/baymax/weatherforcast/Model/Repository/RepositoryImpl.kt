package com.baymax.weatherforcast.Model.Repository

import androidx.lifecycle.LiveData
import com.baymax.weatherforcast.Model.DB.WeatherData
import com.baymax.weatherforcast.Model.Mappers.RecordMapperImpl
import com.baymax.weatherforcast.Model.Network.Response.WeatherResponse
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractions
import com.baymax.weatherforcast.Model.WeatherDataDao
import com.baymax.weatherforcast.Utils.toRecordDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val weatherDao: WeatherDataDao,
    private val weatherNetworkAbstractions: WeatherNetworkAbstractions,
    private val recordMapperImpl: RecordMapperImpl
) : Repository {
    init {
        weatherNetworkAbstractions.downloadedWeather.observeForever {latestWeatherReports->
            persistLatestWeatherReports(latestWeatherReports, recordMapperImpl)
        }
    }
    override suspend fun getWeather(): LiveData<List<WeatherData>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            weatherDao.getAllRecords()
        }
    }

    private fun persistLatestWeatherReports(weatherResponse:WeatherResponse,
                                            recordMapperImpl: RecordMapperImpl){
        GlobalScope.launch(Dispatchers.IO) {
            for(record in weatherResponse.list){
                weatherDao.upsert(recordMapperImpl.toRecordDb(record))
            }
        }
    }

    private suspend fun initWeatherData(){
        weatherNetworkAbstractions.fetchWeather("Hyderabad")
    }
}