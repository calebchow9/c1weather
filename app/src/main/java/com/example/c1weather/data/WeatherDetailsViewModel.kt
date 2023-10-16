package com.example.c1weather.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.c1weather.repository.WeatherRepository
import kotlinx.coroutines.launch

class WeatherDetailsViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {
    val cityState = weatherRepo.cityState

    fun getWeatherFromRepository(cityId: String) {
        viewModelScope.launch {
            try {
                weatherRepo.refreshCityWeather(cityId)
            } catch (e: Exception) {
                e.printStackTrace()
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