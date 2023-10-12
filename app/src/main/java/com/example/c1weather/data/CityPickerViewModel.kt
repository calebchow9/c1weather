package com.example.c1weather.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.c1weather.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.io.IOException

const val UNITS = "metric"
const val API_KEY = "4467831206bb8b3056f38cba080844c0"
// McLean, LA, NYC, Chicago, Houston, Seattle, Honolulu, Denver, SF, Dallas, Portland, Detroit, NOLA
const val GROUP_CITY_IDS = "4772354,5368361,5128581,4887398,4699066,5809844,5856195,5419384,5391959,4684888,5746545,4990729,4335045"

class CityPickerViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {
    // Use backing to protect private ViewModel variables
    // do something with this network variable later in error handling
    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    val cities = weatherRepo.groupWeather

    fun getWeatherFromRepository() {
        viewModelScope.launch {
            try {
                weatherRepo.refreshWeather()
                _eventNetworkError.value = false

            } catch (networkError: IOException) {
                if(cities.value.isNullOrEmpty()) {
                    _eventNetworkError.value = true
                }
            }
        }
    }
}

class CityPickerViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityPickerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}