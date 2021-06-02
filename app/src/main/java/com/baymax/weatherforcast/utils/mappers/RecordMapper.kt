package com.baymax.weatherforcast.utils.mappers

import com.baymax.weatherforcast.ui.fragments.home_fragment.data.RecordDb
import com.baymax.weatherforcast.api.response.City
import com.baymax.weatherforcast.api.response.Record

class RecordMapper {
    fun mapResponseRecord(record: Record, location: City): RecordDb =
        RecordDb(record.dtTxt.filter { it.isDigit() }.toLong(),
            record.dtTxt,
            record.main.humidity,
            record.main.tempMax,
            record.main.tempMin,
            record.main.temp,
            record.wind.deg,
            record.wind.speed,
            record.weather.get(0).description,
            "https://openweathermap.org/img/wn/"+record.weather.get(0).icon+"@2x.png",
            location.coord.lat,
            location.coord.lon,
            location.country,
            location.name,
            location.population,
            location.sunrise,
            location.sunset,
            location.timezone)
}