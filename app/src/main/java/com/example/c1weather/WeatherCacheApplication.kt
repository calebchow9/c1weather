package com.example.c1weather

import android.app.Application
import com.example.c1weather.database.AppDatabase

class WeatherCacheApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}