package com.example.c1weather

import com.example.c1weather.database.CityDetailDataCache
import com.example.c1weather.database.GroupCityDataCache
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.CloudData
import com.example.c1weather.network.CoordinateData
import com.example.c1weather.network.DEFAULT_DOUBLE
import com.example.c1weather.network.DEFAULT_INT
import com.example.c1weather.network.DEFAULT_LONG
import com.example.c1weather.network.DEFAULT_STRING
import com.example.c1weather.network.MainData
import com.example.c1weather.network.SysData
import com.example.c1weather.network.WeatherData
import com.example.c1weather.network.WeatherListData
import com.example.c1weather.network.WeatherResponse
import com.example.c1weather.network.WindData

class FakeItems() {
    val defaultCityDetailDataCache: CityDetailDataCache = CityDetailDataCache(
        id = DEFAULT_LONG,
        cityName = DEFAULT_STRING,
        minTemp = DEFAULT_DOUBLE,
        maxTemp = DEFAULT_DOUBLE,
        humidity = DEFAULT_INT,
        icon = DEFAULT_STRING,
        country = DEFAULT_STRING,
        currentTemp = DEFAULT_DOUBLE,
        currentDescription = DEFAULT_STRING,
        windSpeed = DEFAULT_DOUBLE,
        pressure = DEFAULT_INT,
        sunrise = DEFAULT_LONG,
        sunset = DEFAULT_LONG
    )
    val defaultCityWeatherResponse: CityWeatherResponse = CityWeatherResponse(
        weather=listOf(
            WeatherListData(id = 0, mainWeatherDescription = "", description = "", icon = "")
        )
    )

    val sampleCityWeatherResponse: CityWeatherResponse = CityWeatherResponse(
        coord = CoordinateData(lon = 1.0, lat = 1.0),
        weather=listOf(
            WeatherListData(id = 0, mainWeatherDescription = "Sunny", description = "", icon = "sunny.jpg")
        ),
        base = "base",
        main = MainData(currentTemp = 23.6, minTemp = 23.2, maxTemp = 23.9, humidity = 43, pressure = 23432),
        visibility = 100,
        wind = WindData(speed = 10.0, degree = 320),
        clouds = CloudData(cloudCover = 10.2),
        dt = 23423432,
        sys = SysData(country = "United States", sunrise = 1212323, sunset = 23432432),
        timezone = -5000,
        id = 123123,
        name = "Chicago",
        cod = 123
    )

    val sampleCityDetailDataCache: CityDetailDataCache = CityDetailDataCache(
        id = 1111,
        cityName = "San Francisco",
        country = "United States",
        currentTemp = 34.6,
        minTemp = 34.2,
        maxTemp = 34.9,
        humidity = 23,
        currentDescription = "Cloudy",
        windSpeed = 13.0,
        pressure = 123123,
        sunrise = 321321,
        sunset = 234234,
        icon = "cloudy.jpg"
    )

    val sampleWeatherResponse: WeatherResponse = WeatherResponse(
        cnt = 1,
        list = listOf(
            WeatherData(
                coord = CoordinateData(lon = 1.0, lat = 1.0),
                sys = SysData(country = "United States", sunrise = 1212323, sunset = 23432432),
                weather=listOf(
                    WeatherListData(id = 0, mainWeatherDescription = "Sunny", description = "", icon = "sunny.jpg")
                ),
                main = MainData(currentTemp = 23.6, minTemp = 23.2, maxTemp = 23.9, humidity = 43, pressure = 23432),
                visibility = 23,
                wind = WindData(speed = 10.0, degree = 320),
                clouds = CloudData(cloudCover = 10.2),
                dt = 23423432,
                id = 123123,
                name = "Chicago"
            ),
            WeatherData(
                coord = CoordinateData(lon = 1.0, lat = 1.0),
                sys = SysData(country = "United States", sunrise = 135246, sunset = 13579),
                weather=listOf(
                    WeatherListData(id = 0, mainWeatherDescription = "Sunny", description = "", icon = "sunny.jpg")
                ),
                main = MainData(currentTemp = 43.6, minTemp = 43.2, maxTemp = 43.9, humidity = 23, pressure = 123123),
                visibility = 43,
                wind = WindData(speed = 12.0, degree = 320),
                clouds = CloudData(cloudCover = 13.2),
                dt = 1133557799,
                id = 321321,
                name = "Los Angeles"
            ),
        )
    )

    val sampleGroupCityDataCache: GroupCityDataCache = GroupCityDataCache(
        id = 1234,
        cityName = "McLean",
        country = "United States",
        currentTemp = 10.4,
        minTemp = 10.1,
        maxTemp = 10.8,
        humidity = 10
    )

}