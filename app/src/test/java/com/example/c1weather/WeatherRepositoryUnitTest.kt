package com.example.c1weather

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.c1weather.data.API_KEY
import com.example.c1weather.data.GROUP_CITY_IDS
import com.example.c1weather.data.UNITS
import com.example.c1weather.database.AppDatabase
import com.example.c1weather.database.CityDetailDataCache
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.DEFAULT_DOUBLE
import com.example.c1weather.network.DEFAULT_INT
import com.example.c1weather.network.DEFAULT_LONG
import com.example.c1weather.network.DEFAULT_STRING
import com.example.c1weather.network.NetworkState
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.network.WeatherData
import com.example.c1weather.network.WeatherListData
import com.example.c1weather.network.WeatherResponse
import com.example.c1weather.repository.WeatherRepository
import io.mockk.Awaits
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.runs
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import retrofit2.Response
import kotlin.reflect.jvm.internal.impl.renderer.DescriptorRenderer.ValueParametersHandler.DEFAULT


class WeatherRepositoryUnitTest {
    private val database = mockk<AppDatabase>(relaxed = true)
    private lateinit var repository: WeatherRepository

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @Before
    fun setUp() {
        mockkObject(WeatherApi)
        repository = WeatherRepository(database)
        coEvery { database.groupCityDao().insertAll(any()) } just Runs
        coEvery { database.cityDetailsDao().insert(any()) } just Runs
    }

    @Test
    fun `test repository refreshWeather with successful response`() = runBlocking {
        val testSuccessfulResponse: Response<WeatherResponse> = Response.success(WeatherResponse())
        val expected: NetworkState<List<WeatherData>> = NetworkState.Success(result = listOf())
        coEvery { WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS) } returns testSuccessfulResponse
        repository.refreshWeather()

        assertEquals(expected, repository.groupState.value)
    }

    @Test
    fun `test repository refreshWeather with error response and empty cache`() = runBlocking {
        val testErrorResponse: Response<WeatherResponse> = Response.error(404, ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"key\":[\"error\"]}"
        ))
        val expected: NetworkState<List<WeatherData>> = NetworkState.Error(message = "Response.error()")
        coEvery { database.groupCityDao().doesGroupCitiesExist(any())} returns false
        coEvery { WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS) } returns testErrorResponse
        repository.refreshWeather()

        assertEquals(expected, repository.groupState.value)
    }

    @Test
    fun `test repository refreshWeather with error response but filled cache`() = runBlocking {
        val testErrorResponse: Response<WeatherResponse> = Response.error(404, ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"key\":[\"error\"]}"
        ))
        val expected: NetworkState<List<WeatherData>> = NetworkState.Success(result = listOf())
        coEvery { database.groupCityDao().doesGroupCitiesExist(any())} returns true
        coEvery { database.groupCityDao().getGroupCityWeather() } returns listOf()
        coEvery { WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS) } returns testErrorResponse
        repository.refreshWeather()

        assertEquals(expected, repository.groupState.value)
    }

    @Test
    fun `test repository refreshCityWeather with successful response`() = runBlocking {
        val testSuccessfulResponse: Response<CityWeatherResponse> = Response.success(FakeItems().defaultCityWeatherResponse)
        val expected: NetworkState<CityWeatherResponse> = NetworkState.Success(result = FakeItems().defaultCityWeatherResponse)
        coEvery { WeatherApi.retrofitService.getCityWeather(any(), API_KEY, UNITS) } returns testSuccessfulResponse
        repository.refreshCityWeather("test_cityId")

        assertEquals(expected, repository.cityState.value)
    }

    @Test
    fun `test repository refreshCityWeather with error response and empty cache`() = runBlocking {
        val testErrorResponse: Response<CityWeatherResponse> = Response.error(404, ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"key\":[\"error\"]}"
        ))
        val expected: NetworkState<CityWeatherResponse> = NetworkState.Error(message = "Response.error()")
        coEvery { WeatherApi.retrofitService.getCityWeather(any(), API_KEY, UNITS) } returns testErrorResponse
        repository.refreshCityWeather("test_cityId")

        assertEquals(expected, repository.cityState.value)
    }

    @Test
    fun `test repository refreshCityWeather with error response but filled cache`() = runBlocking {
        val testErrorResponse: Response<CityWeatherResponse> = Response.error(404, ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"key\":[\"error\"]}"
        ))
        val expected: NetworkState<CityWeatherResponse> = NetworkState.Success(result = FakeItems().defaultCityWeatherResponse)
        coEvery { database.cityDetailsDao().doesCityExist(any()) } returns true
        coEvery { database.cityDetailsDao().getWeatherByCityId("test_cityId") } returns FakeItems().defaultCityDetailDataCache
        coEvery { WeatherApi.retrofitService.getCityWeather(any(), API_KEY, UNITS) } returns testErrorResponse
        repository.refreshCityWeather("test_cityId")

        assertEquals(expected, repository.cityState.value)
    }

}