package com.example.c1weather.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.c1weather.repository.WeatherRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

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

     fun convertCelsiusToFahrenheit(celsius: Double) : Int {
        return ((celsius * 9/5) + 32).roundToInt()
    }

    fun createMinMaxString(min: Int, max: Int): String {
        return "$min°/$max°"
    }

    fun convertTimeStampToDate(timestamp: Long, offset: Long): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val date = Date((timestamp + offset) * 1000)
        return sdf.format(date).toString()
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