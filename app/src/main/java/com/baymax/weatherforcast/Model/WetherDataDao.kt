package com.baymax.weatherforcast.Model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.baymax.weatherforcast.Model.DB.*
import com.baymax.weatherforcast.Model.DB.Entity.Record
import com.baymax.weatherforcast.Model.DB.Entity.Weather

@Dao
interface WetherDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(data: Record)
    @Insert
    fun insert(weather:List<Weather>)
    @Transaction
    fun insert(weatherRecord: WeatherRecord){
        upsert(weatherRecord.record)
        insert(weatherRecord.weather)
    }

    @Query("Select * from Record")
    fun getAllRecords():LiveData<WeatherRecord>
}