package com.example.c1weather.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// Data Access Object -- provides r/w access to data (i.e. like SQL on db)
@Dao
interface CityDetailsDao {
    @Query("SELECT EXISTS(SELECT * FROM citydetaildatacache WHERE id = :cityId)")
    fun doesCityExistInDB(cityId: String): Boolean

    @Query("SELECT * FROM citydetaildatacache WHERE id = :cityId")
    fun getWeatherByCityId(cityId: String): CityDetailDataCache

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cacheItem: CityDetailDataCache)

    @Delete
    suspend fun delete(cacheItem: CityDetailDataCache)
}

@Dao
interface GroupCityDao {
    @Query("SELECT * FROM groupcitydatacache")
    fun getGroupCityWeather(): List<GroupCityDataCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cacheItems: List<GroupCityDataCache>)

    @Delete
    suspend fun deleteAll(cacheItems: List<GroupCityDataCache>)
}