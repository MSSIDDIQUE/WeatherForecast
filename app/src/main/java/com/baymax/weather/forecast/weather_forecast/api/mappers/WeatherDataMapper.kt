package com.baymax.weather.forecast.weather_forecast.api.mappers

import com.baymax.weather.forecast.utils.DateTimeUtils.dateTimeFromEpoch
import com.baymax.weather.forecast.utils.DateTimeUtils.dayFromEpoch
import com.baymax.weather.forecast.utils.DateTimeUtils.timeFromEpoch
import com.baymax.weather.forecast.utils.buildIconUrl
import com.baymax.weather.forecast.utils.isRecentWeather
import com.baymax.weather.forecast.utils.isTodayWeather
import com.baymax.weather.forecast.utils.toDegreeCelsius
import com.baymax.weather.forecast.utils.toDegreeFahrenheit
import com.baymax.weather.forecast.utils.toStandardString
import com.baymax.weather.forecast.weather_forecast.api.model.ApiResponseDTO
import com.baymax.weather.forecast.weather_forecast.api.model.WeatherDTO
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherDAO
import com.baymax.weather.forecast.weather_forecast.presentation.model.WeatherReportsDAO

object WeatherDataMapper {

    fun apiResponseMapper(apiResponseDTO: ApiResponseDTO): WeatherReportsDAO {
        val city = apiResponseDTO.city
        val latestTime = apiResponseDTO.list?.sortedBy { it.dt }?.first()?.dt?.timeFromEpoch()
        val hourlyWeatherForecasts = apiResponseDTO.list?.sortedBy { it.dt }?.map { weatherDTO ->
            weatherResponseMapper(weatherDTO)
        }
        return WeatherReportsDAO(
            city = city?.name.orEmpty(),
            country = city?.country.orEmpty(),
            sunriseTime = city?.sunrise?.timeFromEpoch().orEmpty(),
            sunsetTime = city?.sunset?.timeFromEpoch().orEmpty(),
            currentWeather = hourlyWeatherForecasts?.get(0) ?: WeatherDAO(),
            dailyWeatherForecast = hourlyWeatherForecasts?.filter {
                it.time == latestTime
            }.orEmpty(),
            hourlyWeatherForecast = hourlyWeatherForecasts.orEmpty(),
        )
    }

    private fun weatherResponseMapper(weatherResponseDTO: WeatherDTO): WeatherDAO {
        val epoch = weatherResponseDTO.dt
        val weatherDetails = weatherResponseDTO.weather?.get(0)
        val windSpeed = weatherResponseDTO.wind?.speed
        val temperatureDetails = weatherResponseDTO.main
        return WeatherDAO(
            day = epoch?.dayFromEpoch().orEmpty(),
            dateTime = epoch?.dateTimeFromEpoch().orEmpty(),
            time = epoch?.timeFromEpoch().orEmpty(),
            iconSmall = weatherDetails?.icon?.buildIconUrl("@2x.png").orEmpty(),
            iconLarge = weatherDetails?.icon?.buildIconUrl("@4x.png").orEmpty(),
            weatherDescription = weatherDetails?.description?.toStandardString().orEmpty(),
            windSpeed = windSpeed?.let { "${"%.2f".format(it)} km/h" }.orEmpty(),
            humidityLevel = temperatureDetails?.humidity?.let { "$it %" }.orEmpty(),
            temperatureC = temperatureDetails?.temp?.toDegreeCelsius().orEmpty(),
            temperatureF = temperatureDetails?.temp?.toDegreeFahrenheit().orEmpty(),
            maxTemperatureC = temperatureDetails?.tempMax?.toDegreeCelsius().orEmpty(),
            maxTemperatureF = temperatureDetails?.tempMax?.toDegreeFahrenheit().orEmpty(),
            minTemperatureC = temperatureDetails?.tempMin?.toDegreeCelsius().orEmpty(),
            minTemperatureF = temperatureDetails?.tempMin?.toDegreeFahrenheit().orEmpty(),
            isTodayWeatherInfo = epoch?.isTodayWeather() ?: false,
            isRecentWeatherInfo = epoch?.isRecentWeather() ?: false,
        )
    }
}
