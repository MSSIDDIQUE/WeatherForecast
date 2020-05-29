package com.baymax.weatherforcast.Model.Mappers

import com.baymax.weatherforcast.Model.DB.Entity.RecordDb
import com.baymax.weatherforcast.Model.Network.Response.Record

class RecordMapperImpl : RecordMapper {
    override fun mapResponseRecord(record: Record): RecordDb =
        RecordDb(record.dtTxt.filter { it.isDigit() }.toLong(),
            record.dtTxt,
            record.main.humidity,
            record.main.tempMax,
            record.main.tempMin,
            record.main.temp,
            record.wind.deg,
            record.wind.speed,
            record.weather.get(0).description,
            record.weather.get(0).icon)
}