package com.example.c1weather.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherDetailsViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {
    // Use backing to protect private ViewModel variables
    // do something with this network variable later in error handling
    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError
    val cityData = weatherRepo.cityData

    fun getWeatherFromRepository(cityId: String) {
        viewModelScope.launch {
            try {
                weatherRepo.refreshCityWeather(cityId)
                _eventNetworkError.value = false

            } catch (networkError: IOException) {
                if(cityData.value == null) {
                    _eventNetworkError.value = true
                }
            }
        }
    }
}

class WeatherDetailsViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherDetailsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}