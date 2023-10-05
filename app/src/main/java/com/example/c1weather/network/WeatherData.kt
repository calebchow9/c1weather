package com.example.c1weather.network

import com.example.c1weather.database.CityDetailDataCache
import com.example.c1weather.database.GroupCityDataCache
import com.squareup.moshi.Json

// DEFAULT constants
const val DEFAULT_STRING = ""
const val DEFAULT_LONG = 0.toLong()
const val DEFAULT_INT = 0
const val DEFAULT_DOUBLE = 0.0

// GROUP CITY weather data class
data class WeatherResponse (
    val cnt: Int = 0,
    val list: List<WeatherData> = listOf()
) {

    private fun convertToGroupCityDataCache(weatherDataObject: WeatherData): GroupCityDataCache {
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

    private fun convertToCacheModel(weatherObjectList: List<WeatherData>): List<GroupCityDataCache> {
        val listOfWeatherDataCaches = mutableListOf<GroupCityDataCache>()
        for (city: WeatherData in weatherObjectList) {
            listOfWeatherDataCaches.add(convertToGroupCityDataCache(city))
        }
        return listOfWeatherDataCaches
    }
}

// SINGLE CITY weather data class
data class CityWeatherResponse(
    val coord: CoordinateData = CoordinateData(DEFAULT_DOUBLE, DEFAULT_DOUBLE),
    val weather: List<WeatherListData> = listOf(),
    val base: String = DEFAULT_STRING,
    val main: MainData = MainData(DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_INT, DEFAULT_INT),
    val visibility: Int = DEFAULT_INT,
    val wind: WindData = WindData(DEFAULT_DOUBLE, DEFAULT_LONG),
    val clouds: CloudData = CloudData(DEFAULT_DOUBLE),
    val dt: Long = DEFAULT_LONG,
    val sys: SysData = SysData(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_LONG),
    val timezone: Long = DEFAULT_LONG,
    val id: Long = DEFAULT_LONG,
    val name: String = DEFAULT_STRING,
    val cod: Int = DEFAULT_INT
) {
    private fun convertToCacheModel(cityWeatherObject: CityWeatherResponse): CityDetailDataCache {
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

data class WeatherData(
    val coord: CoordinateData = CoordinateData(DEFAULT_DOUBLE, DEFAULT_DOUBLE),
    val sys: SysData = SysData(DEFAULT_STRING, DEFAULT_LONG, DEFAULT_LONG),
    val weather: List<WeatherListData> = listOf(),
    val main: MainData = MainData(DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_INT, DEFAULT_INT),
    val visibility: Int = DEFAULT_INT,
    val wind: WindData = WindData(DEFAULT_DOUBLE, DEFAULT_LONG),
    val clouds: CloudData = CloudData(DEFAULT_DOUBLE),
    val dt: Long = DEFAULT_LONG,
    val id: Long = DEFAULT_LONG,
    val name: String = DEFAULT_STRING
)

data class CoordinateData(
    val lon: Double,
    val lat: Double
)

data class SysData(
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

data class WeatherListData(
    val id: Int,
    @Json(name = "main") val mainWeatherDescription: String,
    val description: String,
    val icon: String
)

data class MainData(
    @Json(name = "temp") val currentTemp: Double,
    @Json(name = "temp_min") val minTemp: Double,
    @Json(name = "temp_max") val maxTemp: Double,
    val pressure: Int,
    val humidity: Int
)
data class WindData(
    val speed: Double,
    @Json(name = "deg") val degree: Long
)

data class CloudData(
    @Json(name = "all") val cloudCover: Double
)