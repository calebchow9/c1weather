package com.example.c1weather

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.c1weather.data.CityPickerViewModel
import com.example.c1weather.data.CityPickerViewModelFactory
import com.example.c1weather.data.WeatherDetailsViewModel
import com.example.c1weather.network.NetworkState
import com.example.c1weather.network.WeatherData
import com.example.c1weather.repository.WeatherRepository
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import org.junit.Test
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule

@OptIn(ExperimentalCoroutinesApi::class)
class CityPickerViewModelUnitTest {
    private val repository = mockk<WeatherRepository>(relaxed = true)
    private lateinit var viewModel: CityPickerViewModel

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        viewModel = CityPickerViewModelFactory(repository).create(CityPickerViewModel::class.java)
    }

    @Test
    fun `viewModel correctly refreshed group weather from repository`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val expected: NetworkState<List<WeatherData>> = NetworkState.Success(result = listOf())
        coEvery { repository.refreshWeather() } answers {
            every { repository.groupState.value } returns expected
        }
        viewModel.getWeatherFromRepository()

        assertEquals(expected, viewModel.groupState.value)
    }

    @Test
    fun `viewModel refreshed group weather from repository with error`() = runTest {
        Dispatchers.setMain(UnconfinedTestDispatcher(testScheduler))
        val expected: NetworkState<List<WeatherData>> = NetworkState.Error(message = "Response.error()")
        coEvery { repository.refreshWeather() } answers {
            every { repository.groupState.value } returns expected
        }
        viewModel.getWeatherFromRepository()

        assertEquals(expected, viewModel.groupState.value)
    }

    @Test
    fun `viewModelFactory successful viewModel creation`() {
        val testViewModel = CityPickerViewModelFactory(repository).create(CityPickerViewModel::class.java)

        assertNotNull(testViewModel)
        assertEquals(testViewModel::class.java, CityPickerViewModel::class.java)
    }

    @Test
    fun `viewModelFactory error viewModel creation`() {
        assertThrows(IllegalArgumentException::class.java) {
            CityPickerViewModelFactory(repository).create(
                WeatherDetailsViewModel::class.java
            )
        }
    }

}