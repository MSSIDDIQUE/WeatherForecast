package com.baymax.weatherforcast.Model.DB.Entity


import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.baymax.weatherforcast.Model.DB.Main
import com.baymax.weatherforcast.Model.DB.Rain
import com.baymax.weatherforcast.Model.DB.Wind
import com.google.gson.annotations.SerializedName

const val weatherId = 0;

@Entity(tableName = "Record")
data class Record(
    @PrimaryKey(autoGenerate = true)
    val rid:Long,
    @SerializedName("dt_txt")
    val dtTxt: String,
    @Embedded(prefix = "main_")
    val main: Main,
    @Embedded(prefix = "rain_")
    val rain: Rain,
    @Embedded(prefix = "wind_")
    val wind: Wind
)
{

}