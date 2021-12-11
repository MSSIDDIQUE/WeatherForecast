package com.baymax.weatherforecast.utils

import android.content.SharedPreferences
import com.baymax.weatherforecast.api.weatherApi.domainModel.ApiResponseDM
import com.baymax.weatherforecast.api.weatherApi.domainModel.WeatherDM
import com.baymax.weatherforecast.api.weatherApi.domainModel.WeatherSummaryDM
import com.baymax.weatherforecast.api.weatherApi.dataTransferModel.ApiResponseDTO
import com.baymax.weatherforecast.api.weatherApi.dataTransferModel.WeatherDTO
import com.baymax.weatherforecast.api.weatherApi.dataTransferModel.WeatherSummaryDTO
import com.baymax.weatherforecast.data.Result
import kotlin.math.roundToInt

inline operator fun <reified T> SharedPreferences.Editor.set(key: String, value: T) {
    when (T::class.java) {
        Int::class.java -> putInt(key, value as Int).apply()
        Long::class.java -> putLong(key, value as Long).apply()
        String::class.java -> putString(key, value as String).apply()
    }
}

inline operator fun <reified T> SharedPreferences.get(key: String, defValue: T): T =
    when (T::class.java) {
        Int::class.java -> getInt(key, defValue as Int) as T
        Long::class.java -> getLong(key, defValue as Long) as T
        String::class.java -> getString(key, defValue as String) as T
        else -> 0 as T
    }


// Non-nullable to Non-nullable
inline fun <I, O> mapList(input: List<I>, mapSingle: (I) -> O): List<O> {
    return input.map { mapSingle(it) }
}

// Nullable to Non-nullable
inline fun <I, O> mapNullInputList(input: List<I>?, mapSingle: (I) -> O): List<O> {
    return input?.map { mapSingle(it) } ?: emptyList()
}

// Non-nullable to Nullable
inline fun <I, O> mapNullOutputList(input: List<I>, mapSingle: (I) -> O): List<O>? {
    return if (input.isEmpty()) null else input.map { mapSingle(it) }
}

inline fun <reified T, reified R> Result<T>.map(transform: (T) -> R): Result<R> {
    return when (this) {
        is Result.Success -> Result.Success(transform(data))
        is Result.Failure -> Result.Failure(msg)
    }
}

inline fun mapApiResponseDTO(
    input: ApiResponseDTO,
    mapWeatherList: (List<WeatherDTO>?) -> List<WeatherDM>
): ApiResponseDM {
    return ApiResponseDM(
        city = ApiResponseDM.CityDM(
            id = input.city.id ?: -1,
            name = input.city.name.orEmpty(),
            cityLat = (input.city.coord.lat ?: (-1)) as Double,
            cityLon = (input.city.coord.lon ?: (-1)) as Double,
            country = input.city.country.orEmpty(),
            sunrise = input.city.sunrise ?: -1,
            sunset = input.city.sunset ?: -1,
            timezone = input.city.timezone ?: -1,
            population = input.city.population ?: -1
        ),
        cnt = input.cnt,
        cod = input.cod.orEmpty(),
        dataGroupedByDate = mapWeatherList(input.list).groupBy { it.dtTxt.split(" ")[0] },
        dataGroupedByTime = mapWeatherList(input.list).groupBy { it.dtTxt.split(" ")[1] },
        message = input.message
    )
}

fun mapWeatherDTO(
    dto: WeatherDTO,
    mapWeatherSummaryList: (List<WeatherSummaryDTO>) -> List<WeatherSummaryDM>
): WeatherDM {
    return WeatherDM(
        dt = dto.dt ?: -1,
        dtTxt = dto.dtTxt.orEmpty(),
        temp = WeatherDM.TempInfoDM(
            feelsLike = (dto.main.feelsLike ?: -1) as Double,
            grndLevel = dto.main.grndLevel ?: -1,
            humidity = (dto.main.humidity ?: -1).toString() + " %",
            pressure = dto.main.pressure ?: -1,
            seaLevel = dto.main.seaLevel ?: -1,
            temp = ((dto.main.temp ?: -1) as Double).toDegreeCelsius(),
            tempKf = (dto.main.tempKf ?: -1) as Double,
            tempMax = ((dto.main.tempMax ?: -1) as Double).toDegreeCelsius(),
            tempMin = ((dto.main.tempMin ?: -1) as Double).toDegreeCelsius()
        ),
        wind = WeatherDM.WindDM(
            angle = dto.wind.deg ?: -1,
            speed = (dto.wind.speed ?: -1) as Double
        ),
        weatherSummary = mapWeatherSummaryList(dto.weather)[0]
    )
}

fun mapWeatherSummaryDTO(
    dto: WeatherSummaryDTO
): WeatherSummaryDM {
    return WeatherSummaryDM(
        description = dto.description.orEmpty(),
        iconSmall = Constants.WEATHER_API_ICONS_BASE_URL + dto.icon.orEmpty() + "@2x.png",
        iconLarge = Constants.WEATHER_API_ICONS_BASE_URL + dto.icon.orEmpty() + "@4x.png",
        id = dto.id ?: -1,
        main = dto.main.orEmpty()
    )
}

fun Double.toDegreeCelsius() = (this - 273.15).roundToInt().toString()