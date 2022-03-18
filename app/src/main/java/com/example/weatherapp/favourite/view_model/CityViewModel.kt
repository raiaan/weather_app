package com.example.weatherapp.favourite.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.models.City
import com.example.weatherapp.repositories.LocalCityRepo
import kotlinx.coroutines.launch

class CityViewModel (private val repository: LocalCityRepo) : ViewModel() {
    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCities: LiveData<List<City>> = repository.allCities.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
     fun delete(city: City) = viewModelScope.launch {
        repository.delete(city)
    }
}