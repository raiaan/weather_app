package com.example.weatherapp.setting

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.weatherapp.R
import java.util.*

class SettingFragment : PreferenceFragmentCompat() {
    var TP: Preference? = null
    var fav: Preference? = null
    var fav_A: Preference? = null
    var Lp: ListPreference? = null
    var language: ListPreference? = null
    lateinit var SP: SharedPreferences
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        SP = PreferenceManager.getDefaultSharedPreferences(context)
        language = findPreference<ListPreference>("language")
        language?.setOnPreferenceChangeListener { preference, newValue ->
            chooseLanguage(preference, newValue as String)
            true
        }
    }
    open fun setLocale(activity: Activity, languageCode: String?): Unit {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources = activity.resources
        val config = resources.configuration
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
        }
        resources.updateConfiguration(config, resources.displayMetrics)
    }
    private fun chooseLanguage(preference: Preference?, newValue: String){
        var item: String = newValue
        if (preference!!.key.equals("language")) {
            when (item) {
                "Arabic" -> {
                    setLocale(context as Activity,"ar")
                    requireActivity().recreate()
                }
                "English" -> {
                    setLocale(context as Activity,"en")
                    requireActivity().recreate()
                }
            }
        }
    }
}