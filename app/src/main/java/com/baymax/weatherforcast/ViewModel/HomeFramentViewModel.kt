package com.baymax.weatherforcast.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.baymax.weatherforcast.Model.DB.WeatherData
import com.baymax.weatherforcast.Model.Repository.Repository
import com.baymax.weatherforcast.Model.Repository.RepositoryImpl
import kotlinx.coroutines.Dispatchers

class HomeFramentViewModel(private val repo: Repository):ViewModel() {
    val weatherData by lazy {
        liveData<WeatherData>(Dispatchers.IO) {
            val data = repo.getWeather()
            emitSource(data)
        }
    }
}