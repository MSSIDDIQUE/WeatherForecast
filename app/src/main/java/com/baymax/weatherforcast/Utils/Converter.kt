package com.baymax.studysolutions.utils

import androidx.room.TypeConverter
import com.baymax.weatherforcast.Model.DB.Entity.RecordDb
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class Converter {
    @TypeConverter
    fun fromString(value: String?): List<RecordDb?>? {
        val listType: Type = object : TypeToken<List<String?>?>() {}.getType()
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayLisr(list: List<RecordDb?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}