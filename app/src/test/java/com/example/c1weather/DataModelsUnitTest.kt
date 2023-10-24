package com.example.c1weather

import com.example.c1weather.database.CityDetailDataCache
import com.example.c1weather.database.GroupCityDataCache
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.DEFAULT_INT
import com.example.c1weather.network.DEFAULT_LONG
import com.example.c1weather.network.DEFAULT_STRING
import com.example.c1weather.network.MainData
import com.example.c1weather.network.SysData
import com.example.c1weather.network.WeatherData
import com.example.c1weather.network.WeatherListData
import com.example.c1weather.network.WindData
import org.junit.Assert.assertEquals
import org.junit.Test

class DataModelsUnitTest {
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
    fun `test CityDetailsDataCache to network model`() {
        val expected = CityWeatherResponse(
            id = 1111,
            name = "San Francisco",
            sys =  SysData(country = "United States", sunrise = 321321, sunset = 234234),
            main = MainData(currentTemp = 34.6, minTemp = 34.2, maxTemp = 34.9, pressure = 123123, humidity = 23),
            weather = listOf(WeatherListData(id = DEFAULT_INT, mainWeatherDescription = "Cloudy", description = DEFAULT_STRING, icon = "cloudy.jpg")),
            wind = WindData(speed = 13.0, degree = DEFAULT_LONG)
        )
        assertEquals(expected, FakeItems().sampleCityDetailDataCache.convertToNetworkModel(FakeItems().sampleCityDetailDataCache))
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

    @Test
    fun `test GroupCityDataCache to network model`() {
        val expected = WeatherData(
            id = 1234,
            name = "McLean",
            sys = SysData(country = "United States", DEFAULT_LONG, DEFAULT_LONG),
            main = MainData(currentTemp = 10.4, minTemp = 10.1, maxTemp = 10.8, humidity = 10, pressure = DEFAULT_INT)
        )
        assertEquals(expected, FakeItems().sampleGroupCityDataCache.convertToNetworkModel(FakeItems().sampleGroupCityDataCache))
    }
}
