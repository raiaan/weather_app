package com.example.weatherapp.home_screen.view_model


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.repositories.OnlineRepository
import com.example.weatherapp.models.WeatherResponse
import kotlinx.coroutines.*

class WeatherViewModel constructor(private val mainRepository: OnlineRepository) : ViewModel() {
    var mutableLiveData= MutableLiveData<WeatherResponse>()
    var favmutableLiveData = MutableLiveData<WeatherResponse>()
    val errorMessage = MutableLiveData<String>()
    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    private val loading = MutableLiveData<Boolean>()
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
    fun getOnlineCityWeather(lat:String , lon:String){
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = mainRepository.getFavWeatherData(lat, lon)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    favmutableLiveData.postValue(response.body())
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