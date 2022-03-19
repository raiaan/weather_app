package com.example.weatherapp.add_fav_city

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.add_fav_city.view_model.AddFavViewModel
import com.example.weatherapp.add_fav_city.view_model.AddFavViewModelFactory
import com.example.weatherapp.databinding.FragmentAddCityBinding
import com.example.weatherapp.models.City
import com.example.weatherapp.util.getLocationFromAddress

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddCityFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCityFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAddCityBinding? = null
    private val binding get() = _binding!!
    private val addFavViewModel: AddFavViewModel by viewModels {
        AddFavViewModelFactory((requireActivity().application as WeatherApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddCityBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSave.setOnClickListener {
            if (TextUtils.isEmpty(binding.addCity.text)) {
            } else {
                insertToFavCity(binding.addCity.text.toString())
                Navigation.findNavController(binding.root).popBackStack()
            }
        }
        binding.cancelBtn.setOnClickListener { Navigation.findNavController(binding.root).popBackStack() }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun insertToFavCity(cityName :String){
        val geoPoint =  getLocationFromAddress(requireContext() ,cityName)
        if (geoPoint != null){
            addFavViewModel.insert( City(
                System.currentTimeMillis(),
                cityName,
                "${geoPoint.latitude}",
                "${geoPoint.longitude}" )
            )
        }

    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddCityFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}