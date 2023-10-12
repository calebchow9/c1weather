package com.example.c1weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.c1weather.data.CityPickerViewModelFactory
import com.example.c1weather.data.WeatherDetailsViewModel
import com.example.c1weather.data.WeatherDetailsViewModelFactory
import com.example.c1weather.databinding.FragmentDetailBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

const val CITY_ID = "cityId"

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var cityId: String
    private val viewModel: WeatherDetailsViewModel by activityViewModels {
        WeatherDetailsViewModelFactory(
            (activity?.application as WeatherCacheApplication).repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            cityId = it.getString(CITY_ID).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun convertCelsiusToFahrenheit(celsius: Double) : Int {
        return ((celsius * 9/5) + 32).roundToInt()
    }

    private fun createMinMaxString(min: Int, max: Int): String {
        return "$min°/$max°"
    }

    private fun convertTimeStampToDate(timestamp: Long, offset: Long): String {
        val sdf = SimpleDateFormat("h:mm a", Locale.ENGLISH)
        val date = Date((timestamp + offset) * 1000)
        return sdf.format(date).toString()
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getWeatherFromRepository(cityId)
        viewModel.cityData.observe(viewLifecycleOwner
        ) {
            Glide.with(view)
                .load("https://openweathermap.org/img/wn/${it.weather[0].icon}@4x.png")
                .into(binding.detailWeatherImageView)
            binding.detailCityNameTextView.text = it.name
            binding.detailCountryTextView.text = it.sys.country
            binding.detailCurrentTempTextView.text = "${convertCelsiusToFahrenheit(it.main.currentTemp)}°"
            binding.detailWeatherConditionsTextView.text = it.weather[0].mainWeatherDescription
            binding.detailMinMaxTempTextView.text = createMinMaxString(convertCelsiusToFahrenheit(it.main.minTemp), convertCelsiusToFahrenheit(it.main.maxTemp))
            binding.detailHumidityCurrentTextTextView.text = "${it.main.humidity}%"
            binding.detailWindSpeedValueTextView.text = "${it.wind.speed} MPH"
            binding.detailPressureValueTextView.text = "${it.main.pressure} hPa"
            binding.detailSunriseValueTextView.text = convertTimeStampToDate(it.sys.sunrise, it.timezone)
            binding.detailSunsetValueTextView.text = convertTimeStampToDate(it.sys.sunset, it.timezone)
        }

    }
}