package com.example.c1weather.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// Data Access Object -- provides r/w access to data (i.e. like SQL on db)
@Dao
interface CityDetailsDao {
    @Query("SELECT * FROM citydetaildatacache WHERE id = :cityId")
    fun getWeatherByCityId(cityId: String): CityDetailDataCache

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cityDetailDataCache: CityDetailDataCache)

    @Update
    suspend fun update(cityDetailDataCache: CityDetailDataCache)
}

@Dao
interface GroupCityDao {
    @Query("SELECT * FROM groupcitydatacache")
    fun getGroupCityWeather(): List<GroupCityDataCache>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(groupCityDataCache: GroupCityDataCache)

    @Update
    suspend fun update(cityDetailDataCache: CityDetailDataCache)
}