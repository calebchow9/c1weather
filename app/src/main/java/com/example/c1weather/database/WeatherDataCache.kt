package com.example.c1weather.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherDataCache (
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
)