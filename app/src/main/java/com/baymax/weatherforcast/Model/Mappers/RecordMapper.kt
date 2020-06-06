package com.baymax.weatherforcast.Model.Mappers

import com.baymax.weatherforcast.Model.DB.Entity.RecordDb
import com.baymax.weatherforcast.Model.Response.City
import com.baymax.weatherforcast.Model.Response.Record

interface RecordMapper {
    fun mapResponseRecord(record:Record,location: City):RecordDb
}