package com.baymax.weatherforcast.Model.DB

import androidx.lifecycle.LiveData
import androidx.room.*
import com.baymax.weatherforcast.Model.DB.*
import com.baymax.weatherforcast.Model.DB.Entity.RecordDb

@Dao
interface WeatherDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(data: RecordDb)

    @Transaction
    fun updateRecord(data: RecordDb) {
        deleteAllRecords()
        upsert(data)
    }

    @Query("Select * from Record")
    fun getAllRecords():LiveData<List<WeatherData>>

    @Query("DELETE from Record")
    fun deleteAllRecords()

}