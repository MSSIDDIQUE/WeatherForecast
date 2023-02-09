package com.baymax.weather.forecast.weather_forecast.presentation.model

data class WeatherReportsDAO(
    val city: String = "",
    val country: String = "",
    val sunriseTime: String = "",
    val sunsetTime: String = "",
    val currentWeather: WeatherDAO = WeatherDAO(),
    val hourlyWeatherForecast: List<WeatherDAO> = emptyList(),
    val dailyWeatherForecast: List<WeatherDAO> = emptyList(),
)
