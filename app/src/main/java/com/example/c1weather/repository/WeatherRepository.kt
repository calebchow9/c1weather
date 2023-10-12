package com.example.c1weather.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.c1weather.data.API_KEY
import com.example.c1weather.data.GROUP_CITY_IDS
import com.example.c1weather.data.UNITS
import com.example.c1weather.database.AppDatabase
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.network.WeatherData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(private val database: AppDatabase) {
    // Use backing to protect private ViewModel variables
    private val _groupWeather = MutableLiveData<List<WeatherData>>()
    val groupWeather: LiveData<List<WeatherData>> = _groupWeather
    private val _cityData = MutableLiveData<CityWeatherResponse>()
    val cityData: LiveData<CityWeatherResponse> = _cityData

    private fun getNetworkGroupWeather(): List<WeatherData> {
        val cacheList = database.groupCityDao().getGroupCityWeather()
        val networkList = mutableListOf<WeatherData>()
        cacheList.forEach{cache -> networkList.add(cache.convertToNetworkModel(cache))}

        return networkList.toList()
    }

    // API call used to refresh offline cache of GROUP weather data (stored as cache model)
    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            try {
                val weatherObj = WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS)
                // update Room cache
                database.groupCityDao().insertAll(weatherObj.convertToCacheModel(weatherObj.list))
            } catch (e: Exception) {

            }
            // get value from Room cache, regardless of if network call was successful
            _groupWeather.postValue(getNetworkGroupWeather())
        }
    }
    // API call used to refresh offline cache of City Detail weather data (stored as cache model)
    suspend fun refreshCityWeather(cityId: String) {
        withContext(Dispatchers.IO) {
            try {
                val cityWeatherObj = WeatherApi.retrofitService.getCityWeather(cityId, API_KEY, UNITS)
                // update Room cache
                database.cityDetailsDao().insert(cityWeatherObj.convertToCacheModel(cityWeatherObj))
            } catch (e: Exception) {

            }
            // get value from Room cache, regardless of if network call was successful
            val cacheItem = database.cityDetailsDao().getWeatherByCityId(cityId)
            _cityData.postValue(cacheItem.convertToNetworkModel(cacheItem))
        }
    }
}