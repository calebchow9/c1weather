package com.example.c1weather.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.c1weather.CityPickerFragmentDirections
import com.example.c1weather.databinding.CityItemBinding
import com.example.c1weather.network.WeatherData
import kotlin.math.roundToInt

class CityAdapter(private val context: Context?, private var dataset: List<WeatherData> = listOf()) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CityViewHolder {
        val itemBinding = CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return CityViewHolder(itemBinding)
    }
    class CityViewHolder(val itemBinding: CityItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateWeatherData(list: List<WeatherData>) {
        dataset = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = dataset.size

    private fun convertCelsiusToFahrenheit(celsius: Double) : Int {
        return ((celsius * 9/5) + 32).roundToInt()
    }

    private fun createMinMaxString(min: Int, max: Int): String {
        return "$min°/$max°"
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = dataset[position]
        holder.itemBinding.cityTextView.text = city.name
        holder.itemBinding.currentTempTextView.text = "${convertCelsiusToFahrenheit(city.main.currentTemp)}°"
        holder.itemBinding.minMaxTempTextView.text = createMinMaxString(convertCelsiusToFahrenheit(city.main.minTemp), convertCelsiusToFahrenheit(city.main.maxTemp))
        holder.itemBinding.humidityTextView.text = "${city.main.humidity}%"

        holder.itemBinding.cityCardView.setOnClickListener {
            val action = CityPickerFragmentDirections.citySelectedAction(cityName = holder.itemBinding.cityTextView.text.toString())
            holder.itemView.findNavController().navigate(action)
        }


    }

}