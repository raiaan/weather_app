package com.example.weatherapp.home_screen.view

import android.content.Context
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeScreenBinding
import com.example.weatherapp.home_screen.view_model.WeatherViewModel
import com.example.weatherapp.home_screen.view_model.WeatherViewModelFactory
import com.example.weatherapp.repositories.OnlineRepository
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.network.RetrofitService
import com.example.weatherapp.network.RetrofitService.Companion.retrofitService
import com.example.weatherapp.util.dateFromLongToStr
import java.util.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeScreenFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var  viewModel: WeatherViewModel
    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var lat:String
    private lateinit var log:String

    val hoursAdapter = HoursAdapter()
    val daysAdapter = DailyAdapter()
    lateinit var  mainRepository:OnlineRepository
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_setting_key), Context.MODE_PRIVATE)?: return
       val retrofitService = RetrofitService.getInstance()
       mainRepository = OnlineRepository(retrofitService, requireContext())
       viewModel = ViewModelProvider(this, WeatherViewModelFactory(mainRepository))
           .get(WeatherViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        viewModel.getOnlineWeatherData()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater,container,false)
        initUI()
        viewModel.mutableLiveData.observe(viewLifecycleOwner,weatherResponse)

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            Log.v("change","there is some error : ${it}")
        })

        viewModel.loading.observe(viewLifecycleOwner,  {
            if (it) {
                //binding.progressDialog.visibility = View.VISIBLE
            } else {
                //binding.progressDialog.visibility = View.GONE
            }
        })
        return binding.root
    }
    private fun getCity(lat:String,lon:String):String{
        viewModel.getOnlineWeatherData()
        val myLocation = Geocoder(requireContext(), Locale.getDefault())
        val myList: List<Address> =
            myLocation.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
        val address: Address = myList[0]
        return address.subAdminArea
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.currentDate.text = dateFromLongToStr(Date(),"EEE MMM d")
        lat = sharedPreferences.getString(context?.resources?.getString(R.string.pref_user_lat) ,"30.044420")!!
        log = sharedPreferences.getString(context?.resources?.getString(R.string.pref_user_longitude),"31.235712")!!
        binding.currentCity.text = getCity(lat,log)
    }

    val weatherResponse:(it:WeatherResponse)->Unit =  {it:WeatherResponse ->
        run {
            Glide.with(this)
                .load("https://openweathermap.org/img/wn/${it.current!!.weather[0].icon}.png")
                .into(binding.currentWeatherIcon)
            binding.currentWeatherFeelLike.text = it.current!!.weather[0].description
            binding.currentWeatherTemp.text = "${it.current!!.temp}"
            binding.currentWeatherHumidity.text = "Humidity: ${it.current!!.humidity}%"
            binding.currentWeatherWind.text = "Wind: ${it.current!!.windSpeed}"
            binding.currentWeatherPressure.text = "Pressure: ${it.current!!.pressure}"
            binding.currentWeatherClouds.text = "Clouds: ${it.current!!.clouds}%"
            hoursAdapter.hours = it.hourly
            daysAdapter.days = it.daily
        }
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

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeScreenFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}