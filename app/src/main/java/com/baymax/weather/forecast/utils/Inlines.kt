package com.baymax.weather.forecast.utils

import android.content.SharedPreferences
import androidx.core.content.edit
import com.baymax.weather.forecast.data.ResponseWrapper
import com.baymax.weather.forecast.weather_forecast.api.data_transfer_model.ApiResponseDTO
import com.baymax.weather.forecast.weather_forecast.api.data_transfer_model.WeatherDTO
import com.baymax.weather.forecast.weather_forecast.api.data_transfer_model.WeatherSummaryDTO
import com.baymax.weather.forecast.weather_forecast.api.domain_model.ApiResponseDM
import com.baymax.weather.forecast.weather_forecast.api.domain_model.WeatherDM
import com.baymax.weather.forecast.weather_forecast.api.domain_model.WeatherSummaryDM
import kotlin.math.roundToInt

operator fun SharedPreferences.set(key: String, value: Any?) = when (value) {
    is String? -> edit { putString(key, value) }
    is Int -> edit { putInt(key, value) }
    is Boolean -> edit { putBoolean(key, value) }
    is Float -> edit { putFloat(key, value) }
    is Long -> edit { putLong(key, value) }
    else -> throw UnsupportedOperationException("Not yet implemented")
}

inline operator fun <reified T : Any> SharedPreferences.get(
    key: String,
    defaultValue: T? = null
): T = when (T::class) {
    String::class -> getString(key, defaultValue as? String ?: "") as T
    Int::class -> getInt(key, defaultValue as? Int ?: -1) as T
    Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T
    Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T
    Long::class -> getLong(key, defaultValue as? Long ?: -1) as T
    else -> throw UnsupportedOperationException("Not yet implemented")
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

inline fun <reified T, reified R> ResponseWrapper<T>.map(transform: (T) -> R): ResponseWrapper<R> {
    return when (this) {
        is ResponseWrapper.Success -> ResponseWrapper.Success(transform(data))
        is ResponseWrapper.Failure -> ResponseWrapper.Failure(msg)
    }
}

inline fun mapApiResponseDTO(
    input: ApiResponseDTO,
    mapWeatherList: (List<WeatherDTO>?) -> List<WeatherDM>
): ApiResponseDM {
    return ApiResponseDM(
        city = ApiResponseDM.CityDM(
            id = input.city?.id ?: -1,
            name = input.city?.name.orEmpty(),
            cityLat = (input.city?.coord?.lat ?: (-1)) as Double,
            cityLon = (input.city?.coord?.lon ?: (-1)) as Double,
            country = input.city?.country.orEmpty(),
            sunrise = input.city?.sunrise ?: -1,
            sunset = input.city?.sunset ?: -1,
            timezone = input.city?.timezone ?: -1,
            population = input.city?.population ?: -1
        ),
        cnt = input.cnt ?: 0,
        cod = input.cod.orEmpty(),
        dataGroupedByDate = mapWeatherList(input.list).groupBy { it.dtTxt.split(" ")[0] },
        dataGroupedByTime = mapWeatherList(input.list).groupBy { it.dtTxt.split(" ")[1] },
        message = input.message ?: 0
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
            feelsLike = (dto.main?.feelsLike ?: -1) as Double,
            grndLevel = dto.main?.grndLevel ?: -1,
            humidity = (dto.main?.humidity ?: -1).toString() + " %",
            pressure = dto.main?.pressure ?: -1,
            seaLevel = dto.main?.seaLevel ?: -1,
            temp = ((dto.main?.temp ?: -1) as Double).toDegreeCelsius(),
            tempKf = (dto.main?.tempKf ?: -1) as Double,
            tempMax = ((dto.main?.tempMax ?: -1) as Double).toDegreeCelsius(),
            tempMin = ((dto.main?.tempMin ?: -1) as Double).toDegreeCelsius()
        ),
        wind = WeatherDM.WindDM(
            angle = dto.wind?.deg ?: -1,
            speed = (dto.wind?.speed ?: -1) as Double
        ),
        weatherSummary = mapWeatherSummaryList(dto.weather ?: listOf())[0]
    )
}

fun mapWeatherSummaryDTO(
    dto: WeatherSummaryDTO
): WeatherSummaryDM {
    return WeatherSummaryDM(
        description = dto.description?.toStandardString().orEmpty(),
        iconSmall = Constants.WEATHER_API_ICONS_BASE_URL + dto.icon.orEmpty() + "@2x.png",
        iconLarge = Constants.WEATHER_API_ICONS_BASE_URL + dto.icon.orEmpty() + "@4x.png",
        id = dto.id ?: -1,
        main = dto.main.orEmpty()
    )
}

fun Double.toDegreeCelsius() = (this - 273.15).roundToInt().toString()
