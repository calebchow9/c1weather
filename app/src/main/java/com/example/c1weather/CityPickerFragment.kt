package com.example.c1weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.c1weather.adapter.CityAdapter
import com.example.c1weather.data.CityPickerViewModel
import com.example.c1weather.databinding.FragmentCityPickerBinding

class CityPickerFragment : Fragment() {
    private var _binding: FragmentCityPickerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CityPickerViewModel by viewModels()

    // Fragment inflated in onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityPickerBinding.inflate(inflater, container, false)

        val recyclerView = binding.weatherRecyclerview
        recyclerView.adapter = viewModel.cities.value?.let { CityAdapter(context, it) }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}