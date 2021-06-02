package com.baymax.weatherforcast.ui.fragments.home_fragment.data

import androidx.lifecycle.LiveData
import androidx.room.*

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