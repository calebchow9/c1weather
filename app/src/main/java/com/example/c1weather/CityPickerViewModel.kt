package com.example.c1weather

import androidx.lifecycle.ViewModel

class CityPickerViewModel : ViewModel() {

    private var _arraylist = arrayListOf(0..100)

    // Use backing to protect private ViewModel variables
    val arraylist: ArrayList<IntRange>
        get() = _arraylist

}