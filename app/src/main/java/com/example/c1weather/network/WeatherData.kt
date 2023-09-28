package com.example.c1weather.network

import com.squareup.moshi.Json


data class WeatherResponse (
    val cnt: Int,
    val list: List<WeatherData>
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
    val timezone: Long,
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
    @Json(name = "temp_max") val maxTemp: Double
)
data class WindData(
    val speed: Double,
    @Json(name = "deg") val degree: Long
)

data class CloudData(
    @Json(name = "all") val cloudCover: Double
)