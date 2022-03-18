package com.example.weatherapp.favourite.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.databinding.FragmentAddCityBinding
import com.example.weatherapp.databinding.FragmentFavouritesListBinding
import com.example.weatherapp.favourite.view_model.CityViewModel
import com.example.weatherapp.favourite.view_model.CityViewModelFactory
import com.example.weatherapp.models.City

/**
 * A fragment representing a list of Items.
 */
class FavouritesFragment : Fragment() {
    val deleteCityCallback:(it: City)->Unit = {it: City -> citiesViewModel.delete(it) }
    val itemClickCallback:(it:City)->Unit = {it:City -> Toast.makeText(requireContext(),it.name,Toast.LENGTH_LONG)}
    private var columnCount = 1
    private val cityAdapter = MyItemRecyclerViewAdapter(deleteCityCallback,itemClickCallback);
    private val citiesViewModel: CityViewModel by viewModels {
        CityViewModelFactory((requireActivity().application as WeatherApplication).repository)
    }
    private var _binding: FragmentFavouritesListBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesListBinding.inflate(inflater,container,false)
        val view = binding.root
        binding.list.apply {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
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
   companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            FavouritesFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}