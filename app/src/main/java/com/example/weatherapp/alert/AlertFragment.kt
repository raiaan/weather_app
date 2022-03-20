package com.example.weatherapp.alert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.alert.view_model.AlertViewModeFactory
import com.example.weatherapp.alert.view_model.AlertViewModel
import com.example.weatherapp.databinding.FragmentAlertBinding
import com.example.weatherapp.databinding.FragmentFavouritesListBinding
import com.example.weatherapp.favourite.view_model.CityViewModel
import com.example.weatherapp.favourite.view_model.CityViewModelFactory
import com.example.weatherapp.models.Alert


class AlertFragment : Fragment() {

    private val viewmodel: AlertViewModel by viewModels {
        AlertViewModeFactory((requireActivity().application as WeatherApplication).alertRepo)
    }

    val deleteLocation:(alert:Alert)->Unit = {alert:Alert ->
        viewmodel.delete(alert)
    }
    private var _binding: FragmentAlertBinding? = null
    private val binding get() = _binding!!
    private val alertAdapter = AlertAdapter(deleteLocation)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAlertBinding.inflate(inflater,container,false)
        binding.alertList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = alertAdapter
        }
        viewmodel.getAlert.observe(viewLifecycleOwner){
            alertAdapter.alert = it
        }
        binding.addAlert.setOnClickListener{
            AlertDialog().show(requireFragmentManager(), AlertDialog.TAG)
        }
        return binding.root
    }
}