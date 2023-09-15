package com.example.c1weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.c1weather.databinding.FragmentCityPickerBinding

class CityPickerFragment : Fragment() {
    private var _binding: FragmentCityPickerBinding? = null
    private val binding get() = _binding!!

    private lateinit var textView: TextView

    // Fragment inflated in onCreateView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityPickerBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        textView = binding.textView
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}