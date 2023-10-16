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
import com.example.c1weather.network.CityWeatherResponse
import com.example.c1weather.network.NetworkState
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getWeatherFromRepository(cityId)
        viewModel.cityState.observe(viewLifecycleOwner
        ) {
            when(it) {
                is NetworkState.Success -> handleSuccess(it.result, view)
                is NetworkState.Error -> showNetworkState(isLoading = false)
                is NetworkState.Loading -> showNetworkState(isLoading = true)
            }
        }
    }

    private fun toggleViews(shouldDisplay: Boolean) {
        binding.detailWeatherImageView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailCityNameTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailCountryTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.countryLineDivider.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailCurrentTempTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailWeatherConditionsTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailMinMaxTempTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailHumidityImageView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailHumidityCurrentTextTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.temperatureDivider.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailWindSpeedTextTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailWindSpeedValueTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailPressureTextTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailPressureValueTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailSunriseTextTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailSunriseValueTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailSunsetTextTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
        binding.detailSunsetValueTextView.visibility = if(shouldDisplay) View.VISIBLE else View.GONE
    }

    private fun showNetworkState(isLoading: Boolean) {
        toggleViews(shouldDisplay = false)
        if (isLoading) {
            binding.detailErrorTextView.visibility = View.GONE
            Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(binding.detailStateImageView)
        } else { // Error state
            Glide.with(this).clear(binding.detailStateImageView)
            binding.detailStateImageView.setBackgroundResource(R.drawable.snag_error)
            binding.detailErrorTextView.visibility = View.VISIBLE
        }
        binding.detailStateImageView.visibility = View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    private fun handleSuccess(data: CityWeatherResponse, view: View) {
        binding.detailErrorTextView.visibility = View.GONE
        binding.detailStateImageView.visibility = View.GONE
        Glide.with(view)
            .load("https://openweathermap.org/img/wn/${data.weather[0].icon}@4x.png")
            .into(binding.detailWeatherImageView)
        binding.detailCityNameTextView.text = data.name
        binding.detailCountryTextView.text = data.sys.country
        binding.detailCurrentTempTextView.text = "${convertCelsiusToFahrenheit(data.main.currentTemp)}°"
        binding.detailWeatherConditionsTextView.text = data.weather[0].mainWeatherDescription
        binding.detailMinMaxTempTextView.text = createMinMaxString(convertCelsiusToFahrenheit(data.main.minTemp), convertCelsiusToFahrenheit(data.main.maxTemp))
        binding.detailHumidityCurrentTextTextView.text = "${data.main.humidity}%"
        binding.detailWindSpeedValueTextView.text = "${data.wind.speed} MPH"
        binding.detailPressureValueTextView.text = "${data.main.pressure} hPa"
        binding.detailSunriseValueTextView.text = convertTimeStampToDate(data.sys.sunrise, data.timezone)
        binding.detailSunsetValueTextView.text = convertTimeStampToDate(data.sys.sunset, data.timezone)
        toggleViews(shouldDisplay = true)
    }
}