package com.example.c1weather.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.c1weather.database.WeatherDataCache
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.WeatherApi
import kotlinx.coroutines.launch

class WeatherDetailsViewModel : ViewModel() {
    // Use backing to protect private ViewModel variables
    private val _cityData = MutableLiveData<CityWeatherResponse>()
    val cityData: LiveData<CityWeatherResponse> = _cityData
    private fun convertToWeatherDataCache(cityWeatherObject: CityWeatherResponse): WeatherDataCache {
        return WeatherDataCache(
            id = cityWeatherObject.id,
            cityName = cityWeatherObject.name,
            country = cityWeatherObject.sys.country,
            currentTemp = cityWeatherObject.main.currentTemp,
            minTemp = cityWeatherObject.main.minTemp,
            maxTemp = cityWeatherObject.main.maxTemp,
            humidity = cityWeatherObject.main.humidity,
            currentDescription = cityWeatherObject.weather[0].mainWeatherDescription,
            windSpeed = cityWeatherObject.wind.speed,
            pressure = cityWeatherObject.main.pressure,
            sunrise = cityWeatherObject.sys.sunrise,
            sunset = cityWeatherObject.sys.sunset
        )
    }

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