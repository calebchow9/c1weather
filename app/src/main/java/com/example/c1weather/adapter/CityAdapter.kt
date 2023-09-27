package com.example.c1weather.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.c1weather.R
import com.example.c1weather.network.WeatherData
import kotlin.math.roundToInt

class CityAdapter(private val context: Context?, private var dataset: List<WeatherData> = listOf()) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cityTextView: TextView = view.findViewById(R.id.city_textView)
        val currentTempTextView: TextView = view.findViewById(R.id.current_temp_textView)
        val minMaxTempTextView: TextView = view.findViewById(R.id.min_max_temp_textView)
        val cloudCoverTextView: TextView = view.findViewById(R.id.cloud_cover_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : CityViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)

        return CityViewHolder(adapterLayout)
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
        holder.cityTextView.text = city.name
        holder.currentTempTextView.text = "${convertCelsiusToFahrenheit(city.main.currentTemp)}°"
        holder.minMaxTempTextView.text = createMinMaxString(convertCelsiusToFahrenheit(city.main.minTemp), convertCelsiusToFahrenheit(city.main.maxTemp))
        holder.cloudCoverTextView.text = "${city.clouds.cloudCover.roundToInt()}%"

    }

}