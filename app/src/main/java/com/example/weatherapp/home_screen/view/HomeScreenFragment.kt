package com.example.weatherapp.home_screen.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeScreenBinding
import com.example.weatherapp.home_screen.view_model.WeatherViewModel
import com.example.weatherapp.home_screen.view_model.WeatherViewModelFactory
import com.example.weatherapp.repositories.OnlineRepository
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.network.RetrofitService
import com.example.weatherapp.util.*
import java.util.*

class HomeScreenFragment : Fragment() {
    private lateinit var  viewModel: WeatherViewModel
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lat:String
    private lateinit var log:String

    private val hoursAdapter = HoursAdapter()
    private val daysAdapter = DailyAdapter()
    private lateinit var  mainRepository:OnlineRepository
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().applicationContext.getSharedPreferences(
            getString(R.string.preference_setting_key), Context.MODE_PRIVATE)?: return
       val retrofitService = RetrofitService.getInstance()
       mainRepository = OnlineRepository(retrofitService, requireContext())
       viewModel = ViewModelProvider(this, WeatherViewModelFactory(mainRepository))[WeatherViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        if (isNetworkAvailable(requireContext()))
            viewModel.getOnlineWeatherData()
        else {
            updateUI(readFromSharedPref())
            Toast.makeText(context,resources.getString(R.string.no_internet),Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater,container,false)
        initUI()
        viewModel.mutableLiveData.observe(viewLifecycleOwner,weatherResponse)

        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Log.v("change", "there is some error : $it")
        }

        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) {
                //binding.progressDialog.visibility = View.VISIBLE
            } else {
                //binding.progressDialog.visibility = View.GONE
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lat = sharedPreferences.getString(context?.resources?.getString(R.string.pref_user_lat) ,"30.044420")!!
        log = sharedPreferences.getString(context?.resources?.getString(R.string.pref_user_longitude),"31.235712")!!
        binding.currentCity.text = getCity(lat,log,requireContext())
        if (isNetworkAvailable(requireContext()))
            viewModel.getOnlineWeatherData()
        else readFromSharedPref()
    }

    val weatherResponse:(it:WeatherResponse)->Unit =  {it:WeatherResponse ->
        run {
            updateUI(it)
            saveDataTosharedPref(it)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun updateUI(it:WeatherResponse){
        Glide.with(this)
            .load("https://openweathermap.org/img/wn/${it.current!!.weather[0].icon}.png")
            .into(binding.currentWeatherIcon)
        binding.currentWeatherFeelLike.text = it.current!!.weather[0].description
        binding.currentWeatherTemp.text = "${it.current!!.temp}"
        binding.currentWeatherHumidity.text = resources.getString(R.string.humidity_label) + it.current!!.humidity +resources.getString(R.string.precent_symbol)
        binding.currentWeatherWind.text =resources.getString(R.string.wind_label)+ it.current!!.windSpeed
        binding.currentWeatherClouds.text = resources.getString(R.string.humidity_label) +it.current!!.clouds+resources.getString(R.string.precent_symbol)
        hoursAdapter.hours = it.hourly
        daysAdapter.days = it.daily
    }
    private fun saveDataTosharedPref(it:WeatherResponse){
        with(sharedPreferences.edit()){
            putString(getString(R.string.pref_last_update),convertWeatherResponseToGson(it))
            apply()
        }
    }
    private fun readFromSharedPref():WeatherResponse{
        println("from home fragment: "+sharedPreferences.getString(resources.getString(R.string.pref_last_update),null))
        return convertGsonToWeatherResponse(
            sharedPreferences.getString(resources.getString(R.string.pref_last_update),null)!!
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initUI(){
        binding.weatherHoursList.apply {
            adapter = hoursAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }
        binding.dailyForcastList.apply {
            adapter = daysAdapter
            layoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
        }
        binding.settingFab.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_homeScreenFragment_to_settingFragment)
        }
    }

}