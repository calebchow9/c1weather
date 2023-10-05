package com.example.c1weather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.DEFAULT_INT
import com.example.c1weather.network.DEFAULT_LONG
import com.example.c1weather.network.DEFAULT_STRING
import com.example.c1weather.network.MainData
import com.example.c1weather.network.SysData
import com.example.c1weather.network.WeatherData
import com.example.c1weather.network.WeatherListData
import com.example.c1weather.network.WeatherResponse
import com.example.c1weather.network.WindData

@Entity
data class CityDetailDataCache (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val cityName: String,
    val country: String,
    val currentTemp: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val humidity: Int,
    val currentDescription: String,
    val windSpeed: Double,
    val pressure: Int,
    val sunrise: Long,
    val sunset: Long
) {
    private fun convertToNetworkModel(cache: CityDetailDataCache): CityWeatherResponse {
        return CityWeatherResponse(
            id = cache.id,
            name = cache.cityName,
            sys = SysData(country = cache.country, sunrise = cache.sunrise, sunset = cache.sunset),
            main = MainData(currentTemp = cache.currentTemp, minTemp = cache.minTemp, maxTemp = cache.maxTemp, humidity = cache.humidity, pressure = cache.pressure),
            weather = listOf(WeatherListData(id = DEFAULT_INT, mainWeatherDescription = cache.currentDescription, description = DEFAULT_STRING, icon = DEFAULT_STRING)),
            wind = WindData(speed = cache.windSpeed, degree = DEFAULT_LONG)
        )
    }
}

@Entity
data class GroupCityDataCache (
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val cityName: String,
    val country: String,
    val currentTemp: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val humidity: Int
){
    private fun convertToNetworkModel(groupCityDataCacheList: List<GroupCityDataCache>): WeatherResponse {
        val list = mutableListOf<WeatherData>()
        for (city: GroupCityDataCache in groupCityDataCacheList) {
            list.add(
                WeatherData(
                    id = city.id,
                    name = city.cityName,
                    sys = SysData(country = city.country, DEFAULT_LONG, DEFAULT_LONG),
                    main = MainData(currentTemp = city.currentTemp, minTemp = city.minTemp, maxTemp = city.maxTemp, humidity = city.humidity, pressure = DEFAULT_INT)
                )
            )
        }
        return WeatherResponse(list = list)
    }
}