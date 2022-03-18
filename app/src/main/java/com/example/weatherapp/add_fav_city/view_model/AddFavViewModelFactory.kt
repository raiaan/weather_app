package com.example.weatherapp.add_fav_city.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.favourite.view_model.CityViewModel
import com.example.weatherapp.repositories.LocalCityRepo

class AddFavViewModelFactory(private val repository: LocalCityRepo) : ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFavViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddFavViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}