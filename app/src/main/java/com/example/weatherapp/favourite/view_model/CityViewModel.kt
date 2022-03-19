package com.example.weatherapp.favourite.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.City
import com.example.weatherapp.repositories.LocalCityRepo
import kotlinx.coroutines.launch

class CityViewModel (private val repository: LocalCityRepo) : ViewModel() {
    val allCities: LiveData<List<City>> = repository.allCities.asLiveData()
     fun delete(city: City) = viewModelScope.launch {
        repository.delete(city)
    }
}