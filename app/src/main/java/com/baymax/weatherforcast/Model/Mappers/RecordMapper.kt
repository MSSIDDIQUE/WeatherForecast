package com.baymax.weatherforcast.Model.Mappers

import com.baymax.weatherforcast.Model.DB.Entity.RecordDb
import com.baymax.weatherforcast.Model.Network.Response.City
import com.baymax.weatherforcast.Model.Network.Response.Record

interface RecordMapper {
    fun mapResponseRecord(record:Record,location: City):RecordDb
}