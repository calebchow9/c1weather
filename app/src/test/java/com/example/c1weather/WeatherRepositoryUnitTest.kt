package com.example.c1weather

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.c1weather.data.API_KEY
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
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
object LiveDataTestUtil {
    /**
     * Get the value from a LiveData object. We're waiting for LiveData to emit, for 2 seconds.
     * Once we got a notification via onChanged, we stop observing.
     */
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T? {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer: Observer<T?> = object : Observer<T?> {
            override fun onChanged(o: T?) {
                data[0] = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(2, TimeUnit.SECONDS)
        @Suppress("UNCHECKED_CAST")
        return data[0] as T?
    }
}
class WeatherRepositoryUnitTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun testRefreshWeatherSuccessfulResponse() = runBlocking {
        val testSuccessfulResponse: Response<WeatherResponse> = Response.success(WeatherResponse())
        val expected: NetworkState<List<WeatherData>> = NetworkState.Success(result = listOf())

        // setup mock database and DAO
        val groupCityDao = mockk<GroupCityDao>()
        coEvery { groupCityDao.insertAll(any()) } just Runs
        val database = mockk<AppDatabase>()
        coEvery { database.groupCityDao() } returns groupCityDao

        // mock RetrofitAPI object
        mockkObject(WeatherApi)
        coEvery { WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS) } returns testSuccessfulResponse

        val testRepository = WeatherRepository(database)

        testRepository.refreshWeather()
        val result = LiveDataTestUtil.getValue(testRepository.groupState)

        assertEquals(expected, result)
    }

    @Test
    fun testRefreshWeatherErrorResponseEmptyCache() = runBlocking {
        val testErrorResponse: Response<WeatherResponse> = Response.error(404, ResponseBody.create(
            MediaType.parse("application/json"),
            "{\"key\":[\"error\"]}"
        ))
        val expected: NetworkState<List<WeatherData>> = NetworkState.Error(message = "Response.error()")

        // setup mock database and DAO
        val groupCityDao = mockk<GroupCityDao>()
        coEvery { groupCityDao.insertAll(any()) } just Runs
        coEvery { groupCityDao.doesGroupCitiesExist(any())} returns false
        val database = mockk<AppDatabase>()
        coEvery { database.groupCityDao() } returns groupCityDao

        // mock RetrofitAPI object
        mockkObject(WeatherApi)
        coEvery { WeatherApi.retrofitService.getWeather(GROUP_CITY_IDS, API_KEY, UNITS) } returns testErrorResponse

        val testRepository = WeatherRepository(database)

        testRepository.refreshWeather()
        val result = LiveDataTestUtil.getValue(testRepository.groupState)

        assertEquals(expected, result)
    }


}