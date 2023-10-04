package com.example.c1weather.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.c1weather.database.WeatherDataCache
import com.example.c1weather.database.WeatherDao

class WeatherCacheViewModel(private val weatherDao: WeatherDao): ViewModel() {
    fun citiesWeather(): List<WeatherDataCache> = weatherDao.getAllCitiesWeather()
    fun cityDetailWeather(cityId: String): WeatherDataCache = weatherDao.getWeatherByCityId(cityId)
}

// instantiates our ViewModel (rather than in a fragment) -- can handle all lifecycle events
class WeatherCacheViewModelFactory(
    private val weatherDao: WeatherDao
) : ViewModelProvider.Factory {
    // creates Factory
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherCacheViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherCacheViewModel(weatherDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}