package com.example.c1weather

import com.example.c1weather.database.CityDetailDataCache
import com.example.c1weather.database.GroupCityDataCache
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherDataUnitTest {
    @Test
    fun `test CityWeatherResponse to cached model`() {
        val expected = CityDetailDataCache(
            id = 123123,
            cityName = "Chicago",
            country = "United States",
            currentTemp = 23.6,
            minTemp = 23.2,
            maxTemp = 23.9,
            humidity = 43,
            currentDescription = "Sunny",
            windSpeed = 10.0,
            pressure = 23432,
            sunrise = 1212323,
            sunset = 23432432,
            icon = "sunny.jpg"
        )
        assertEquals(expected, FakeItems().sampleCityWeatherResponse.convertToCacheModel(FakeItems().sampleCityWeatherResponse))
    }

    @Test
    fun `test WeatherResponse to cached model`() {
        val expected = listOf(
            GroupCityDataCache(
                id = 123123,
                cityName = "Chicago",
                country = "United States",
                currentTemp = 23.6,
                minTemp = 23.2,
                maxTemp = 23.9,
                humidity = 43
            ),
            GroupCityDataCache(
                id = 321321,
                cityName = "Los Angeles",
                country = "United States",
                currentTemp = 43.6,
                minTemp = 43.2,
                maxTemp = 43.9,
                humidity = 23
            )
        )
        assertEquals(expected, FakeItems().sampleWeatherResponse.convertToCacheModel(FakeItems().sampleWeatherResponse.list))
    }
}
