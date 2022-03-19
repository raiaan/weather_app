package com.example.weatherapp.home_screen.view_model


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.repositories.OnlineRepository
import com.example.weatherapp.models.WeatherResponse
import kotlinx.coroutines.*

class WeatherViewModel constructor(private val mainRepository: OnlineRepository) : ViewModel() {
    var mutableLiveData= MutableLiveData<WeatherResponse>()
    val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()
    fun getOnlineWeatherData(){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getWeatherData()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    mutableLiveData.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response.headers()} ")
                }
            }
        }
    }
    private fun onError(message: String) {
        errorMessage.postValue(message)
        loading.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}