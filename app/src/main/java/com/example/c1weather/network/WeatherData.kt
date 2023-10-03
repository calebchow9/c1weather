package com.example.c1weather.network

import com.squareup.moshi.Json


data class WeatherResponse (
    val cnt: Int,
    val list: List<WeatherData>
)

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
)

// MAIN weather data class
data class WeatherData(
    val coord: CoordinateData,
    val sys: SysData,
    val weather: List<WeatherListData>,
    val main: MainData,
    val visibility: Int,
    val wind: WindData,
    val clouds: CloudData,
    val dt: Long,
    val id: Long,
    val name: String
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