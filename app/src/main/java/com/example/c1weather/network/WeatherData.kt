package com.example.c1weather.network

import com.example.c1weather.database.CityDetailDataCache
import com.example.c1weather.database.GroupCityDataCache
import com.squareup.moshi.Json

// GROUP CITY weather data class
data class WeatherResponse (
    val cnt: Int = 0,
    val list: List<WeatherData> = listOf()
) {
    private fun convertGroupCityDataCacheToWeatherResponse(groupCityDataCacheList: List<GroupCityDataCache>): WeatherResponse {
        val list = mutableListOf<WeatherData>()
        for (city: GroupCityDataCache in groupCityDataCacheList) {
            list.add(
                WeatherData(
                    id = city.id,
                    name = city.cityName,
                    sys = SysData(country = city.country, 0, 0),
                    main = MainData(currentTemp = city.currentTemp, minTemp = city.minTemp, maxTemp = city.maxTemp, humidity = city.humidity, pressure = 0)
                )
            )
        }
        return WeatherResponse(list = list)
    }
}

// SINGLE CITY weather data class
data class CityWeatherResponse(
    val coord: CoordinateData = CoordinateData(0.0, 0.0),
    val weather: List<WeatherListData> = listOf(),
    val base: String = "",
    val main: MainData = MainData(0.0, 0.0, 0.0, 0, 0),
    val visibility: Int = 0,
    val wind: WindData = WindData(0.0, 0),
    val clouds: CloudData = CloudData(0.0),
    val dt: Long = 0,
    val sys: SysData = SysData("", 0, 0),
    val timezone: Long = 0,
    val id: Long = 0,
    val name: String = "",
    val cod: Int = 0
) {
    private fun convertCityDetailDataCacheToCityWeatherResponse(cache: CityDetailDataCache): CityWeatherResponse {
        return CityWeatherResponse(
            id = cache.id,
            name = cache.cityName,
            sys = SysData(country = cache.country, sunrise = cache.sunrise, sunset = cache.sunset),
            main = MainData(currentTemp = cache.currentTemp, minTemp = cache.minTemp, maxTemp = cache.maxTemp, humidity = cache.humidity, pressure = cache.pressure),
            weather = listOf(WeatherListData(id = 0, mainWeatherDescription = cache.currentDescription, description = "", icon = "")),
            wind = WindData(speed = cache.windSpeed, degree = 0)
        )
    }
}

data class WeatherData(
    val coord: CoordinateData = CoordinateData(0.0, 0.0),
    val sys: SysData = SysData("", 0, 0),
    val weather: List<WeatherListData> = listOf(),
    val main: MainData = MainData(0.0, 0.0, 0.0, 0, 0),
    val visibility: Int = 0,
    val wind: WindData = WindData(0.0, 0),
    val clouds: CloudData = CloudData(0.0),
    val dt: Long = 0,
    val id: Long = 0,
    val name: String = ""
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