package com.example.weatherapp.favourite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.databinding.FragmentFavouritesListBinding
import com.example.weatherapp.favourite.view_model.CityViewModel
import com.example.weatherapp.favourite.view_model.CityViewModelFactory
import com.example.weatherapp.models.City

/**
 * A fragment representing a list of Items.
 */
class FavouritesFragment : Fragment() {
    private val deleteCityCallback:(it: City)->Unit = { it: City -> citiesViewModel.delete(it) }
    private val itemClickCallback:(it:City)->Unit = { it:City ->
        val bundle = Bundle()
        bundle.putSerializable(resources.getString(R.string.favourite_city_key),it)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_favouritesFragment_to_favouriteCityWeatherFragment ,bundle)
    }
    private val cityAdapter = MyItemRecyclerViewAdapter(deleteCityCallback,itemClickCallback)
    private val citiesViewModel: CityViewModel by viewModels {
        CityViewModelFactory((requireActivity().application as WeatherApplication).repository)
    }
    private var _binding: FragmentFavouritesListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesListBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.list.apply {
            layoutManager =  LinearLayoutManager(context)
            adapter = cityAdapter
        }
        citiesViewModel.allCities.observe(viewLifecycleOwner){
            cityAdapter.city = it
        }
        binding.fab.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_favouritesFragment_to_addCityFragment)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}