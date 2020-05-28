package com.baymax.weatherforcast.Model.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.baymax.weatherforcast.Model.DB.Entity.Record
import com.baymax.weatherforcast.Model.DB.Entity.Weather
import com.baymax.weatherforcast.Model.WetherDataDao

@Database(
    entities = [Record::class, Weather::class],
    version = 1
)
abstract class MyDatabase: RoomDatabase() {
    abstract fun weatherDataDao() : WetherDataDao

    companion object{
        @Volatile private var instance: MyDatabase ?= null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(LOCK){
            instance?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
            MyDatabase::class.java, "weather.db")
                .build()
    }
}