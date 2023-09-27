package com.example.c1weather.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.c1weather.CityPickerFragment
import com.example.c1weather.R
import com.example.c1weather.citymodel.City
import com.example.c1weather.network.WeatherResponse

class CityAdapter(private val context: Context?, private val dataset: MutableList<WeatherResponse>) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val cityTextView: TextView = view.findViewById(R.id.city_textView)
        val stateTextView: TextView = view.findViewById(R.id.state_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)

        return CityViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = dataset[position]
        Log.d("TESTER", city.toString())
        holder.cityTextView.text = city.location.cityName
        holder.stateTextView.text = city.location.stateName
    }

}