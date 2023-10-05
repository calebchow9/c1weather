package com.example.c1weather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.WeatherData

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
    private fun convertToWeatherDataCache(cityWeatherObject: CityWeatherResponse): CityDetailDataCache {
        return CityDetailDataCache(
            id = cityWeatherObject.id,
            cityName = cityWeatherObject.name,
            country = cityWeatherObject.sys.country,
            currentTemp = cityWeatherObject.main.currentTemp,
            minTemp = cityWeatherObject.main.minTemp,
            maxTemp = cityWeatherObject.main.maxTemp,
            humidity = cityWeatherObject.main.humidity,
            currentDescription = cityWeatherObject.weather[0].mainWeatherDescription,
            windSpeed = cityWeatherObject.wind.speed,
            pressure = cityWeatherObject.main.pressure,
            sunrise = cityWeatherObject.sys.sunrise,
            sunset = cityWeatherObject.sys.sunset
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
    private fun convertToWeatherDataCache(weatherDataObject: WeatherData): GroupCityDataCache {
        return GroupCityDataCache(
            id = weatherDataObject.id,
            cityName = weatherDataObject.name,
            country = weatherDataObject.sys.country,
            currentTemp = weatherDataObject.main.currentTemp,
            minTemp = weatherDataObject.main.minTemp,
            maxTemp = weatherDataObject.main.maxTemp,
            humidity = weatherDataObject.main.humidity
        )
    }

    private fun convertToListOfWeatherDataCache(weatherObjectList: List<WeatherData>): List<GroupCityDataCache> {
        val listOfWeatherDataCaches = mutableListOf<GroupCityDataCache>()
        for (city: WeatherData in weatherObjectList) {
            listOfWeatherDataCaches.add(convertToWeatherDataCache(city))
        }
        return listOfWeatherDataCaches
    }
}