package com.example.c1weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.c1weather.adapter.CityAdapter
import com.example.c1weather.data.CityPickerViewModel
import com.example.c1weather.data.CityPickerViewModelFactory
import com.example.c1weather.databinding.FragmentCityPickerBinding
import com.example.c1weather.network.NetworkState

class CityPickerFragment : Fragment() {
    private var _binding: FragmentCityPickerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CityPickerViewModel by activityViewModels {
        CityPickerViewModelFactory(
            (activity?.application as WeatherCacheApplication).repository
        )
    }
    private lateinit var cityAdapter: CityAdapter
    private lateinit var recyclerView: RecyclerView

    // Fragment inflated in onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityPickerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = binding.weatherRecyclerview
        cityAdapter = CityAdapter(context)
        recyclerView.adapter = cityAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        // observe LiveData for when list gets updated
        viewModel.groupState.observe(viewLifecycleOwner
        ) {
            when(it) {
                is NetworkState.Success -> {
                    cityAdapter.updateWeatherData(it.result)
                    showDataViews()
                }
                is NetworkState.Error -> showNetworkState(isLoading = false)
                is NetworkState.Loading -> showNetworkState(isLoading = true)
            }
        }
        viewModel.getWeatherFromRepository()
    }

    private fun showNetworkState(isLoading: Boolean) {
        binding.weatherAppTextview.visibility = View.GONE
        binding.weatherRecyclerview.visibility = View.GONE
        if (isLoading) {
            binding.errorTextView.visibility = View.GONE
            Glide.with(this)
                .asGif()
                .load(R.drawable.loading)
                .into(binding.stateImageView)
        } else { // Error state
            Glide.with(this).clear(binding.stateImageView)
            binding.stateImageView.setBackgroundResource(R.drawable.snag_error)
            binding.errorTextView.visibility = View.VISIBLE
        }
        binding.stateImageView.visibility = View.VISIBLE
    }

    private fun showDataViews() {
        binding.stateImageView.visibility = View.GONE
        binding.errorTextView.visibility = View.GONE
        binding.weatherRecyclerview.visibility = View.VISIBLE
        binding.weatherAppTextview.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}