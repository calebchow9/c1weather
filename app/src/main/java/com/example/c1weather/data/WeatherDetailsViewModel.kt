package com.example.c1weather.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.WeatherApi
import kotlinx.coroutines.launch

class WeatherDetailsViewModel : ViewModel() {
    // Use backing to protect private ViewModel variables
    private val _cityData = MutableLiveData<CityWeatherResponse>()
    val cityData: LiveData<CityWeatherResponse> = _cityData

    fun getCityWeather(cityId: String) {
        viewModelScope.launch {
            try {
                // call GET endpoint
                val cityWeatherResponseObject = WeatherApi.retrofitService.getCityWeather(cityId, API_KEY, UNITS)
                // add new WeatherResponse to our liveData list
                _cityData.value = cityWeatherResponseObject
            } catch (e: Exception) {
                // default constructor
                _cityData.value = CityWeatherResponse()
            }
        }
    }

}