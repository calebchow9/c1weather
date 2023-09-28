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
    private lateinit var cityAdapter: CityAdapter

    // Fragment inflated in onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityPickerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = binding.weatherRecyclerview
        cityAdapter = CityAdapter(context)
        recyclerView.adapter = cityAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        // observe LiveData for when list gets updated
        viewModel.cities.observe(viewLifecycleOwner
        ) {
            cityAdapter.updateWeatherData(it)
        }
        viewModel.getWeather()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}