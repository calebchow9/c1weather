package com.example.c1weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.c1weather.data.API_KEY
import com.example.c1weather.data.CityPickerViewModel
import com.example.c1weather.data.GROUP_CITY_IDS
import com.example.c1weather.data.UNITS
import com.example.c1weather.database.AppDatabase
import com.example.c1weather.database.GroupCityDao
import com.example.c1weather.network.NetworkState
import com.example.c1weather.network.WeatherApi
import com.example.c1weather.network.WeatherData
import com.example.c1weather.network.WeatherResponse
import com.example.c1weather.repository.WeatherRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import org.junit.Test
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import retrofit2.Response

class CityPickerViewModelUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetWeatherFromRepository() = runTest {
        val testDispatcher = UnconfinedTestDispatcher(testScheduler)
        Dispatchers.setMain(testDispatcher)

        val testSuccessfulResponse: Response<WeatherResponse> = Response.success(WeatherResponse())
        val expected: NetworkState<List<WeatherData>> = NetworkState.Success(result = listOf())

        // setup mock database and DAO
        val groupCityDao = mockk<GroupCityDao>()
        coEvery { groupCityDao.insertAll(any()) } just Runs
        val database = mockk<AppDatabase>()
        coEvery { database.groupCityDao() } returns groupCityDao
        val repository = WeatherRepository(database)
        // mock RetrofitAPI object
        mockkObject(WeatherApi)
        coEvery { WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS) } returns testSuccessfulResponse

        val testViewModel = CityPickerViewModel(repository)
        testViewModel.getWeatherFromRepository()
        val result = LiveDataTestUtil.getValue(testViewModel.groupState)

        assertEquals(expected, result)
    }
}