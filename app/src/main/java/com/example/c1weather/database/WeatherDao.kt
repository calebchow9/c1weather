package com.example.c1weather.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// Data Access Object -- provides r/w access to data (i.e. like SQL on db)
@Dao
interface WeatherDao {
    @Query("SELECT * FROM weatherdatacache")
    fun getAllCitiesWeather(): List<WeatherDataCache>

    @Query("SELECT * FROM weatherdatacache WHERE id = :cityId")
    fun getWeatherByCityId(cityId: String): WeatherDataCache

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(weatherDataCache: WeatherDataCache)

    @Update
    suspend fun update(weatherDataCache: WeatherDataCache)
}