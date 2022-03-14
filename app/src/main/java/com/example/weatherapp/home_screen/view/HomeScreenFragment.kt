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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeScreenBinding
import com.example.weatherapp.home_screen.view_model.WeatherViewModel
import com.example.weatherapp.home_screen.view_model.WeatherViewModelFactory
import com.example.weatherapp.models.OnlineRepository
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.network.RetrofitService
import com.example.weatherapp.util.SpinningLinearLayoutManager
import com.example.weatherapp.util.dateFromLongToStr
import java.text.SimpleDateFormat
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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
    private val linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
    private lateinit var lat:String
    private lateinit var log:String
    val hoursAdapter = HoursAdapter()
    val daysAdapter = DailyAdapter()
    val spinningLinearLayoutManager = SpinningLinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        sharedPreferences = requireActivity().getSharedPreferences(
            getString(R.string.preference_setting_key), Context.MODE_PRIVATE)?: return
        lat = sharedPreferences.getString(getString(R.string.pref_user_lat),"30.044420")!!
        log = sharedPreferences.getString(getString(R.string.pref_user_longitude),"31.235712")!!
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeScreenBinding.inflate(inflater,container,false)
        val view = binding.root
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = OnlineRepository(retrofitService,lat,log)
        binding.weatherHoursList.apply {
            adapter = hoursAdapter
            layoutManager = linearLayoutManager
        }
        binding.dailyForcastList.apply {
            adapter = daysAdapter

        }
        viewModel = ViewModelProvider(this, WeatherViewModelFactory(mainRepository))
            .get(WeatherViewModel::class.java)
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
        viewModel.getOnlineWeatherData()
        return view
    }
    private fun getCity(lat:String,lon:String):String{
        val myLocation = Geocoder(requireContext(), Locale.getDefault())
        val myList: List<Address> =
            myLocation.getFromLocation(lat.toDouble(), lon.toDouble(), 1)
        val address: Address = myList[0]
        return address.subAdminArea
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.currentDate.text = dateFromLongToStr(Date(),"EEE MMM d")
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


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeScreenFragment.
         */
        // TODO: Rename and change types and number of parameters
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