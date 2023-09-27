package com.example.c1weather.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c1weather.citymodel.City
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.network.WeatherResponse
import kotlinx.coroutines.launch

class CityPickerViewModel : ViewModel() {
    private val _cities = MutableLiveData<MutableList<WeatherResponse>>()
    val cities: LiveData<MutableList<WeatherResponse>> = _cities


    // Use backing to protect private ViewModel variables
    val cityNames: List<String>
        get() = listOf("Chicago")

    init {
        getWeather()
    }

    private fun getWeather() {
        viewModelScope.launch {
            try {
                for (city: String in cityNames) {
                    // call GET endpoint
                    val weatherResponseObject = WeatherApi.retrofitService.getWeather(city)
                    Log.d("TESTER", "Success: Retrieved weather data for city $city")
                    _cities.value?.add(weatherResponseObject)
                }

                // add new WeatherResponse to our liveData list
            } catch (e: Exception) {
                 Log.e("ERROR", "Failure: ${e.message}")
            }
        }
    }

}