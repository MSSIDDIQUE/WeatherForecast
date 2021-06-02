package com.baymax.weatherforcast.ui.fragments.home_fragment.data


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "Record")
data class RecordDb(
    @PrimaryKey(autoGenerate = true)
    val rid:Long,
    @SerializedName("dt_txt")
    val dtTxt: String,
    @SerializedName("humidity")
    val humidity:Int,
    @SerializedName("temp_max")
    val tempMax: Double,
    @SerializedName("temp_min")
    val tempMin:Double,
    val temp:Double,
    val deg: Int,
    val speed: Double,
    @SerializedName("description")
    val weather_description: String,
    @SerializedName("icon")
    val weather_icon: String,
    val lat:Double,
    val lon:Double,
    val country: String,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)
{

}