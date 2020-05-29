package com.baymax.weatherforcast.Model.DB

import androidx.room.ColumnInfo

class WeatherData(
    @ColumnInfo(name = "temp")
    val temp : Double,
    @ColumnInfo(name = "tempMin")
    val min_temp:Double,
    @ColumnInfo(name = "tempMax")
    val max_temp:Double,
    @ColumnInfo(name = "humidity")
    val humidity:Int,
    @ColumnInfo(name = "weather_description")
    val weather_desc:String,
    @ColumnInfo(name = "weather_icon")
    val weather_icon:String,
    @ColumnInfo(name = "speed")
    val wind_speed:Double,
    @ColumnInfo(name = "dtTxt")
    val date_time:String
) {

}