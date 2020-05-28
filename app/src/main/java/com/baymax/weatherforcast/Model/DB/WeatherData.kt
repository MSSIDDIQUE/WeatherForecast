package com.baymax.weatherforcast.Model.DB

import androidx.room.ColumnInfo

class WeatherData(
    @ColumnInfo(name = "main_temp")
    val temp : Double,
    @ColumnInfo(name = "main_tempMin")
    val min_temp:Double,
    @ColumnInfo(name = "main_tempMax")
    val max_temp:Double,
    @ColumnInfo(name = "main_humidity")
    val humidity:Int,
    @ColumnInfo(name = "weather_description")
    val weather_desc:String,
    @ColumnInfo(name = "weather_icon")
    val weather_icon:String,
    @ColumnInfo(name = "wind_speed")
    val wind_speed:Double,
    @ColumnInfo(name = "dtTxt")
    val date_time:String
) {

}