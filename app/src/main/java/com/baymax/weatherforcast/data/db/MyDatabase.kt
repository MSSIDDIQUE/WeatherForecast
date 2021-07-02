package com.baymax.weatherforcast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.baymax.weatherforcast.ui.fragments.home_fragment.data.RecordDb

@Database(
    entities = [RecordDb::class],
    version = 1
)
abstract class MyDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: MyDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MyDatabase::class.java, "weather.db"
            ).build()
    }
}