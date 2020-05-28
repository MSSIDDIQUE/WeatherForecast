package com.baymax.weatherforcast.Model.DB.Entity


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.baymax.weatherforcast.Model.DB.Entity.Record

@Entity(
    tableName = "weather",
    foreignKeys = [
    ForeignKey(
        entity = Record::class,
        parentColumns = ["rid"],
        childColumns = ["wid"],
        onDelete = CASCADE
    )
])
data class Weather(
    @PrimaryKey(autoGenerate = true)
    val wid:Long,
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)