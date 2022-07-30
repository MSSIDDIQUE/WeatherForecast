package com.baymax.weather.forecast.utils

class Constants {
    companion object {
        const val WEATHER_API_BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val GOOGLE_PLACE_API_BASE_URL = "https://maps.googleapis.com/maps/api/place/"
        const val WEATHER_API_ICONS_BASE_URL = "https://openweathermap.org/img/wn/"
        const val UPDATE_INTERVAL_SECS = 10L
        const val FASTEST_UPDATE_INTERVAL_SECS = 2L
    }
}