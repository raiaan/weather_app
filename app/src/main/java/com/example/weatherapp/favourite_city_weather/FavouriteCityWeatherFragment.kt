package com.example.weatherapp.favourite_city_weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentFavouriteCityWeatherBinding
import com.example.weatherapp.home_screen.view.DailyAdapter
import com.example.weatherapp.home_screen.view.HoursAdapter
import com.example.weatherapp.home_screen.view_model.WeatherViewModel
import com.example.weatherapp.home_screen.view_model.WeatherViewModelFactory
import com.example.weatherapp.models.City
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.network.RetrofitService
import com.example.weatherapp.repositories.OnlineRepository
import com.example.weatherapp.util.isNetworkAvailable

class FavouriteCityWeatherFragment : Fragment() {
    private var _binding: FragmentFavouriteCityWeatherBinding? = null
    private val binding get() = _binding!!
    private lateinit var  viewModel: WeatherViewModel
    private var city: City? = null
    private val hoursAdapter = HoursAdapter()
    private val daysAdapter = DailyAdapter()
    private lateinit var  mainRepository: OnlineRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            city = it.getSerializable(resources.getString(R.string.favourite_city_key)) as City?
        }
        val retrofitService = RetrofitService.getInstance()
        mainRepository = OnlineRepository(retrofitService, requireContext())
        ViewModelProvider(this, WeatherViewModelFactory(mainRepository))[WeatherViewModel::class.java].also { viewModel = it }
    }

    override fun onStart() {
        super.onStart()
        if (isNetworkAvailable(requireContext()))
            viewModel.getOnlineCityWeather(lat = city!!.lat, lon = city!!.lon)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteCityWeatherBinding.inflate(inflater,container,false)
        initUI()
        viewModel.favmutableLiveData.observe(viewLifecycleOwner,weatherResponse)
        return binding.root
    }
    val weatherResponse:(it:WeatherResponse)->Unit =  {it:WeatherResponse ->
        run {
            updateUI(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.favCurrentCity.text = city!!.name
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(it: WeatherResponse){
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${it.current!!.weather[0].icon}.png")
            .into(binding.favCurrentWeatherIcon)
        binding.favCurrentWeatherFeelLike.text = it.current!!.weather[0].description
        binding.favCurrentWeatherTemp.text = "${it.current!!.temp}"
        binding.favCurrentWeatherHumidity.text = resources.getString(R.string.humidity_label)+it.current!!.humidity+ resources.getString(R.string.precent_symbol)
        binding.favCurrentWeatherWind.text =  resources.getString(R.string.wind_label)+it.current!!.windSpeed
        binding.favCurrentWeatherPressure.text =  resources.getString(R.string.pressure_label)+it.current!!.pressure
        binding.favCurrentWeatherClouds.text =  resources.getString(R.string.clouds_label)+it.current!!.clouds + resources.getString(R.string.precent_symbol)
        hoursAdapter.hours = it.hourly
        daysAdapter.days = it.daily
    }
    private fun initUI(){
        binding.favWeatherHoursList.apply {
            adapter = hoursAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        }
        binding.favDailyForcastList.apply {
            adapter = daysAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        }
    }
}