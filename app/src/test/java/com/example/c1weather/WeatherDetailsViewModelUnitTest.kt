package com.example.c1weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.c1weather.data.CityPickerViewModel
import com.example.c1weather.data.CityPickerViewModelFactory
import com.example.c1weather.data.WeatherDetailsViewModel
import com.example.c1weather.data.WeatherDetailsViewModelFactory
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.NetworkState
import com.example.c1weather.repository.WeatherRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherDetailsViewModelUnitTest {
    private val repository = mockk<WeatherRepository>(relaxed = true)
    private lateinit var viewModel: WeatherDetailsViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = WeatherDetailsViewModelFactory(repository).create(WeatherDetailsViewModel::class.java)
    }

    @Test
    fun `viewModel correctly refreshed city weather from repository`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val expected: NetworkState<CityWeatherResponse> = NetworkState.Success(result = FakeItems().defaultCityWeatherResponse)
        coEvery { repository.refreshCityWeather(any()) } answers {
            every { repository.cityState.value } returns expected
        }
        viewModel.getWeatherFromRepository("test_cityId")

        assertEquals(expected, viewModel.cityState.value)
    }

    @Test
    fun `viewModel refreshed city weather from repository and received error`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val expected: NetworkState<CityWeatherResponse> = NetworkState.Error(message = "Response.error()")
        coEvery { repository.refreshCityWeather(any()) } answers {
            every { repository.cityState.value } returns expected
        }
        viewModel.getWeatherFromRepository("test_cityId")

        assertEquals(expected, viewModel.cityState.value)
    }

    @Test
    fun `viewModelFactory successful viewModel creation`() {
        val testViewModel = WeatherDetailsViewModelFactory(repository).create(WeatherDetailsViewModel::class.java)

        Assert.assertNotNull(testViewModel)
        assertEquals(testViewModel::class.java, WeatherDetailsViewModel::class.java)
    }

    @Test
    fun `viewModelFactory error viewModel creation`() {
        Assert.assertThrows(IllegalArgumentException::class.java) {
            WeatherDetailsViewModelFactory(repository).create(
                CityPickerViewModel::class.java
            )
        }
    }

    @Test
    fun `convert 23 celsius to fahrenheit successfully`() {
        val expected = 73 // 73.4 but rounded to nearest Int
        assertEquals(expected, viewModel.convertCelsiusToFahrenheit(23.0))
    }

    @Test
    fun `create MinMax string successfully`() {
        val expected = "32°/64°"
        assertEquals(expected, viewModel.createMinMaxString(32, 64))
    }

    @Test
    fun `convert millisecond timestamp to date successfully`() {
        val expected = "2:47 AM"
        assertEquals(expected, viewModel.convertTimeStampToDate(1698173693272, 0))
    }
}