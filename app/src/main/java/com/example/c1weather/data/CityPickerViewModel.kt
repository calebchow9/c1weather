package com.example.c1weather.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.c1weather.repository.WeatherRepository
import kotlinx.coroutines.launch

const val UNITS = "metric"
const val API_KEY = "4467831206bb8b3056f38cba080844c0"
// McLean, LA, NYC, Chicago, Houston, Seattle, Honolulu, Denver, SF, Dallas, Portland, Detroit, NOLA
const val GROUP_CITY_IDS = "4772354,5368361,5128581,4887398,4699066,5809844,5856195,5419384,5391959,4684888,5746545,4990729,4335045"

class CityPickerViewModel(private val weatherRepo: WeatherRepository) : ViewModel() {
    val groupState = weatherRepo.groupState

    fun getWeatherFromRepository() {
        viewModelScope.launch {
            try {
                weatherRepo.refreshWeather()
            } catch (e: Exception) {
                e.printStackTrace()
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