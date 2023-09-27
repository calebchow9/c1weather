package com.example.c1weather.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WeatherResponse (
    val location: LocationData,
    val current: CurrentData,
    val forecast: ForecastData
)

data class LocationData(
    @Json(name = "name") val cityName: String,
    @Json(name = "region") val stateName: String
)
data class CurrentData (
    @Json(name = "temp_f") val currentTemp: Double
)

data class ForecastData(
    @Json(name = "forecastday") val forecastDay: List<ForecastDayData>
)

data class ForecastDayData (
    val day: DayWeatherData
)
data class DayWeatherData (
    @Json(name = "maxtemp_f") val maxTemp: Double,
    @Json(name = "mintemp_f") val minTemp: Double,
    @Json(name = "daily_chance_of_rain") val chanceOfPrecipitation: Double
)