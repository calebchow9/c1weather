package com.example.c1weather

import com.example.c1weather.repository.WeatherRepository
import io.mockk.coEvery
import org.junit.Test
import io.mockk.mockk
import kotlinx.coroutines.runBlocking

class CityPickerViewModelUnitTest {
    @Test
    fun testGetWeatherFromRepository(): Unit = runBlocking {
        val repository = mockk<WeatherRepository>()
        coEvery { repository.refreshWeather() }
    }
}