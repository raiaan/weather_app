package com.example.weatherapp.add_fav_city.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.City
import com.example.weatherapp.repositories.LocalCityRepo
import kotlinx.coroutines.launch

class AddFavViewModel  (private val repository: LocalCityRepo) : ViewModel() {

    fun insert(city: City) = viewModelScope.launch {
        repository.insert(city)
    }
}