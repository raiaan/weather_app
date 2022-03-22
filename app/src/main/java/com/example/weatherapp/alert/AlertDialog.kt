package com.example.weatherapp.alert

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.weatherapp.R
import com.example.weatherapp.WeatherApplication
import com.example.weatherapp.alert.view_model.AlertViewModeFactory
import com.example.weatherapp.alert.view_model.AlertViewModel
import com.example.weatherapp.databinding.AlertDialogBinding
import com.example.weatherapp.models.Hourly
import com.example.weatherapp.models.WeatherResponse
import com.example.weatherapp.util.convertGsonToWeatherResponse
import com.example.weatherapp.util.dateFromLongToStr
import com.example.weatherapp.util.getDate
import com.example.weatherapp.util.getRandomInt
import java.util.*

class AlertDialog: DialogFragment() {
    private val viewModel: AlertViewModel by viewModels {
        AlertViewModeFactory((requireActivity().application as WeatherApplication).alertRepo)
    }
    private lateinit var sharedPref:SharedPreferences
    private var _binding: AlertDialogBinding? = null
    private val binding get() = _binding!!
    //lateinit var alarmService: AlarmService
    lateinit var alarmHours: List<Hourly>
    var x: Long = 0
    var startTime: String = ""
    var y: Long = 0
    var endTime: String = ""
    var event: String = ""
    var alarm = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AlertDialogBinding.inflate(inflater,container,false)
        sharedPref =  requireActivity().applicationContext.getSharedPreferences(getString(R.string.preference_setting_key), Context.MODE_PRIVATE)
        alarmHours = getCachedWeatherData().hourly
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        alarmService = AlarmService(
//            requireContext()
//        )
        val events = resources.getStringArray(R.array.events)
        val myAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, events)
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.eventsSpinner.adapter = myAdapter
        binding.eventsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?, view: View, i: Int, l: Long
            ) {
                val e = resources.getStringArray(R.array.events)
                event = e[i]
            }
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
        setupClickListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun setupClickListeners() {
        binding.timePicker.setOnClickListener {
            setAlarm {
                x = it
                binding.timeTxt.text = getDate(it , "EEE MMM d")
            }
        }
        binding.submit.setOnClickListener {
            Log.v("test_time","$x")
            val code = getRandomInt()
            viewModel.addAlertToDB(x, event, code)
            viewModel.addToWorkManager(x,event,code,requireContext())
            val alertFragment = AlertFragment()
            fragmentManager?.beginTransaction()?.replace(R.id.home_fragment_container, alertFragment)
                ?.addToBackStack(null)?.commit()
            dismiss()

        }
    }

    private fun setAlarm(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog( requireContext(), 0, { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(context, 0, { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis)
                        }, this.get(Calendar.HOUR_OF_DAY), this.get(Calendar.MINUTE), false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
    private fun getCachedWeatherData(): WeatherResponse{
        return convertGsonToWeatherResponse(
            sharedPref.getString(requireContext().resources.getString(R.string.pref_last_update),null)!!
        )
    }
    companion object {
        const val TAG = "DialogWithData"
    }
}