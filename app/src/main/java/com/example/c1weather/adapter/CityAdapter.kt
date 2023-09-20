package com.example.c1weather.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.c1weather.CityPickerFragment
import com.example.c1weather.R
import com.example.c1weather.citymodel.City

class CityAdapter(private val context: CityPickerFragment, private val dataset: List<City>) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    class CityViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.city_textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)

        return CityViewHolder(adapterLayout)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = dataset[position]
        holder.textView.text = city.cityName
    }

}