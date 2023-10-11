package com.example.c1weather.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.c1weather.data.API_KEY
import com.example.c1weather.data.GROUP_CITY_IDS
import com.example.c1weather.data.UNITS
import com.example.c1weather.database.AppDatabase
import com.example.c1weather.database.GroupCityDao
import com.example.c1weather.database.GroupCityDataCache
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.network.WeatherData
import com.example.c1weather.network.WeatherResponse

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GroupWeatherRepository(private val database: AppDatabase) {

    private val _groupWeather: MutableLiveData<List<WeatherData>> = MutableLiveData(getNetworkGroupWeather())
    val groupWeather: LiveData<List<WeatherData>> = _groupWeather

    private fun getNetworkGroupWeather(): List<WeatherData> {
        val cacheList = database.groupCityDao().getGroupCityWeather()
        val networkList = mutableListOf<WeatherData>()
        cacheList.forEach{cache -> networkList.add(cache.convertToNetworkModel(cache))}

        return networkList.toList()
    }

    // API call used to refresh offline cache of weather data (stored as cache model)
    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            val weatherObj = WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS)

            // update Room cache
            database.groupCityDao().insertAll(weatherObj.convertToCacheModel(weatherObj.list))
        }
    }
}