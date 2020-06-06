package com.baymax.weatherforcast.Model.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.baymax.weatherforcast.Model.DB.WeatherData
import com.baymax.weatherforcast.Model.Mappers.RecordMapperImpl
import com.baymax.weatherforcast.Model.Response.WeatherResponse
import com.baymax.weatherforcast.Model.Network.WeatherNetworkAbstractions
import com.baymax.weatherforcast.Model.DB.WeatherDataDao
import com.baymax.weatherforcast.Utils.Providers.LocationProvider
import com.baymax.weatherforcast.Utils.toRecordDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryImpl(
    private val weatherDao: WeatherDataDao,
    private val weatherNetworkAbstractions: WeatherNetworkAbstractions,
    private val recordMapperImpl: RecordMapperImpl,
    private val locationProvider: LocationProvider
) : Repository {
    init {
        weatherNetworkAbstractions.downloadedWeather.observeForever {latestWeatherReports->
            persistLatestWeatherReports(latestWeatherReports, recordMapperImpl)
            Log.d("(Saquib)", "The data is successfully saved into the database")
        }
    }
    override suspend fun getWeather(): LiveData<List<WeatherData>> {
        return withContext(Dispatchers.IO){
            initWeatherData()
            Log.d("(Saquib)", "getting the latest data from the database")
            weatherDao.getAllRecords()
        }
    }

    private fun persistLatestWeatherReports(weatherResponse:WeatherResponse,
                                            recordMapperImpl: RecordMapperImpl){
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("(Saquib)","The size of weather response is "+weatherResponse.list.size)
            for(i in 0..(weatherResponse.list.size-1)){
                if(i==0) {
                    weatherDao.updateRecord(recordMapperImpl.toRecordDb(weatherResponse.list.get(i),weatherResponse.city))
                }
                else{
                    weatherDao.upsert(recordMapperImpl.toRecordDb(weatherResponse.list.get(i),weatherResponse.city))
                }
            }
        }
    }

    private suspend fun initWeatherData(){
        val lastWeatherLocation = weatherDao.getAllRecords().value
        if(lastWeatherLocation == null || locationProvider.hasLocationChanged(lastWeatherLocation)) {
            weatherNetworkAbstractions.fetchWeather(locationProvider.getPreferredLocationString())
            return
        }
    }
}