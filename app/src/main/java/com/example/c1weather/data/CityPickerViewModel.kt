package com.example.c1weather.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.c1weather.database.AppDatabase
import com.example.c1weather.database.GroupCityDao
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.network.WeatherData
import com.example.c1weather.repository.GroupWeatherRepository
import kotlinx.coroutines.launch
import java.io.IOException

const val UNITS = "metric"
const val API_KEY = "4467831206bb8b3056f38cba080844c0"
// McLean, LA, NYC, Chicago, Houston, Seattle, Honolulu, Denver, SF, Dallas, Portland, Detroit, NOLA
const val GROUP_CITY_IDS = "4772354,5368361,5128581,4887398,4699066,5809844,5856195,5419384,5391959,4684888,5746545,4990729,4335045"

class CityPickerViewModel(private val groupWeatherRepo: GroupWeatherRepository) : ViewModel() {
    // Use backing to protect private ViewModel variables
//    private val _cities = MutableLiveData<List<WeatherData>>()
//    val cities: LiveData<List<WeatherData>> = _cities
    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError


    val cities = groupWeatherRepo.groupWeather


    fun getWeatherFromRepository() {
        viewModelScope.launch {
            try {
                groupWeatherRepo.refreshWeather()
                _eventNetworkError.value = false

            } catch (networkError: IOException) {
                if(cities.value.isNullOrEmpty()) {
                    _eventNetworkError.value = true
                }
            }
        }
    }

    init {
        getWeatherFromRepository()
    }

//    fun getWeather() {
//        viewModelScope.launch {
//            try {
//                // call GET endpoint
//                val weatherResponseObject = WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS)
//                // add new WeatherResponse to our liveData list
//                _cities.value = weatherResponseObject.list
//            } catch (e: Exception) {
//                // empty list exception case
//                 _cities.value = listOf()
//            }
//        }
//    }

}

class CityPickerViewModelFactory(
    private val repository: GroupWeatherRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityPickerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityPickerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}