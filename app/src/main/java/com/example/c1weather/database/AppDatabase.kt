package com.example.c1weather.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GroupCityDataCache::class, CityDetailDataCache::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun groupCityDao(): GroupCityDao
    abstract fun cityDetailsDao(): CityDetailsDao
    // create only one instance of db
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }


}