package com.ersiver.newsster.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.ersiver.newsster.R
import com.ersiver.newsster.util.PREF_MODE_KEY

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == PREF_MODE_KEY) {
            if ((preference as SwitchPreferenceCompat).isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        return super.onPreferenceTreeClick(preference)
    }
}