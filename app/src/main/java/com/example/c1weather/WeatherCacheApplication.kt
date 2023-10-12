package com.example.c1weather

import android.app.Application
import com.example.c1weather.database.AppDatabase
import com.example.c1weather.repository.WeatherRepository

class WeatherCacheApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: WeatherRepository by lazy { WeatherRepository(database)}
}