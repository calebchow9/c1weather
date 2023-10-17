package com.example.c1weather.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.c1weather.network.NetworkState
import com.example.c1weather.data.API_KEY
import com.example.c1weather.data.GROUP_CITY_IDS
import com.example.c1weather.data.UNITS
import com.example.c1weather.database.AppDatabase
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.network.WeatherData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherRepository(private val database: AppDatabase) {
    // Use backing to protect private ViewModel variables
    private val _groupState = MutableLiveData<NetworkState<List<WeatherData>>>()
    val groupState: LiveData<NetworkState<List<WeatherData>>> = _groupState
    private val _cityState = MutableLiveData<NetworkState<CityWeatherResponse>>()
    val cityState: LiveData<NetworkState<CityWeatherResponse>> = _cityState

    private fun getNetworkGroupWeather(): List<WeatherData> {
        val cacheList = database.groupCityDao().getGroupCityWeather()
        val networkList = mutableListOf<WeatherData>()
        cacheList.forEach{cache -> networkList.add(cache.convertToNetworkModel(cache))}

        return networkList.toList()
    }

    // API call used to refresh offline cache of GROUP weather data (stored as cache model)
    suspend fun refreshWeather() {
        withContext(Dispatchers.IO) {
            _groupState.postValue(NetworkState.Loading)
            try {
                val response = WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS)
                if (response.isSuccessful) {
                    val weatherResponse = response.body()!!
                    // pass weather through state object
                    _groupState.postValue(NetworkState.Success(result = weatherResponse.list))
                    // update Room cache
                    database.groupCityDao().insertAll(weatherResponse.convertToCacheModel(weatherResponse.list))
                } else {
                    // if network call failed, check if we still have cached data
                    val listOfCities = GROUP_CITY_IDS.split(",").map{ it.trim() }
                    if (database.groupCityDao().doesGroupCitiesExist(listOfCities)) {
                        _groupState.postValue(NetworkState.Success(result = getNetworkGroupWeather()))
                    } else {
                        // no cached items and bad api call = error
                        _groupState.postValue(NetworkState.Error(message = response.message()))
                    }
                }
            } catch (e: HttpException) {
                _groupState.postValue(NetworkState.Error(message = e.toString()))
            }
        }
    }
    // API call used to refresh offline cache of City Detail weather data (stored as cache model)
    suspend fun refreshCityWeather(cityId: String) {
        withContext(Dispatchers.IO) {
            _cityState.postValue(NetworkState.Loading)
            try {
                val response = WeatherApi.retrofitService.getCityWeather(cityId, API_KEY, UNITS)
                if(response.isSuccessful) {
                    val cityWeatherResponse = response.body()!!
                    _cityState.postValue(NetworkState.Success(result = cityWeatherResponse))
                    // update Room cache
                    database.cityDetailsDao().insert(cityWeatherResponse.convertToCacheModel(cityWeatherResponse))
                } else {
                    // if network call failed, check if we still have cached data
                    if (database.cityDetailsDao().doesCityExist(cityId)) {
                        val cacheItem = database.cityDetailsDao().getWeatherByCityId(cityId)
                        _cityState.postValue(NetworkState.Success(result = cacheItem.convertToNetworkModel(cacheItem)))
                    } else {
                        // no cached items and bad api call = error
                        _cityState.postValue(NetworkState.Error(message = response.message()))
                    }
                }

            } catch (e: Exception) {
                _cityState.postValue(NetworkState.Error(message = e.toString()))
            }
        }
    }
}