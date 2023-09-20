package com.example.c1weather

import androidx.lifecycle.ViewModel
import com.example.c1weather.citymodel.City

class CityPickerViewModel : ViewModel() {

    private fun populateList() : List<City> {
        val cities = mutableListOf<City>()

        for(i in 0..99) {
            cities.add(City("City: $i"))
        }

        return cities
    }

    private var _cities = populateList()

    // Use backing to protect private ViewModel variables
    val cities: List<City>
        get() = _cities

}