package com.baymax.weatherforcast.Model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.baymax.weatherforcast.Model.DB.*
import com.baymax.weatherforcast.Model.DB.Entity.RecordDb

@Dao
interface WeatherDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(data: RecordDb)

    @Query("Select * from Record")
    fun getAllRecords():LiveData<List<WeatherData>>
}