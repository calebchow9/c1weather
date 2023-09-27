package com.example.c1weather.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c1weather.citymodel.City
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.network.WeatherData
import com.example.c1weather.network.WeatherResponse
import kotlinx.coroutines.launch

class CityPickerViewModel : ViewModel() {
    // Use backing to protect private ViewModel variables
    private val _cities = MutableLiveData<List<WeatherData>>()
    val cities: LiveData<List<WeatherData>> = _cities

    // McLean, LA, NYC, Chicago, Houston, Seattle, Honolulu, Denver, SF, Dallas, Portland, Detroit, NOLA
    private val cityIds = listOf("4772454", "5368361", "5128581", "4887398", "4699066", "5809844", "5856195", "5419384", "5391959", "4684888", "5746545", "4990729", "4335024")
    private val apiKey = "4467831206bb8b3056f38cba080844c0"

    init {
        getWeather()
    }

    private fun getWeather() {
        viewModelScope.launch {
            try {
                val cityIdString = cityIds.joinToString()
                // call GET endpoint
                val weatherResponseObject = WeatherApi.retrofitService.getWeather(cityIdString, apiKey, "metric")
                Log.d("TESTER", "Success: Retrieved weather data for city list $cityIdString.")
                // add new WeatherResponse to our liveData list
                _cities.value = weatherResponseObject.list

            } catch (e: Exception) {
                 Log.e("ViewModel", "Failure: ${e.message}")
            }
        }
    }

}