package com.baymax.weatherforcast.Model.DB

import androidx.room.Embedded
import androidx.room.Relation
import com.baymax.weatherforcast.Model.DB.Entity.Record
import com.baymax.weatherforcast.Model.DB.Entity.Weather

class WeatherRecord (
    @Embedded
    val record: Record,
    @Relation(parentColumn = "rid", entityColumn = "wid")
    val weather:List<Weather>
)