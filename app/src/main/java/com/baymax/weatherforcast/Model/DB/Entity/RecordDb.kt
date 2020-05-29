package com.baymax.weatherforcast.Model.DB.Entity


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "Record")
data class RecordDb(
    @PrimaryKey(autoGenerate = false)
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
    val weather_icon: String
)
{

}