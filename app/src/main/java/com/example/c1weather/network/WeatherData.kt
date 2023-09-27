package com.example.c1weather.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WeatherResponse (
    val list: List<WeatherData>
)

data class WeatherData(
    val name: String,
    val main: MainData,
    val clouds: CloudData
)

data class MainData(
    @Json(name = "temp") val currentTemp: Double,
    @Json(name = "temp_min") val minTemp: Double,
    @Json(name = "temp_max") val maxTemp: Double
)

data class CloudData(
    @Json(name = "all") val cloudCover: Double
)