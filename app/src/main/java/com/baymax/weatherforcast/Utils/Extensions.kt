package com.baymax.weatherforcast.Utils

import com.baymax.weatherforcast.Model.Mappers.RecordMapperImpl
import com.baymax.weatherforcast.Model.Network.Response.Record
import com.baymax.weatherforcast.Model.Network.Response.Weather

fun RecordMapperImpl.toRecordDb(record:Record) = mapResponseRecord(record)