package com.ersiver.newsster.ui.settings

import android.os.Bundle
import android.view.View
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

    /**
     * Set the top padding of the PreferenceScreen's,
     * so the list of preferences is below the toolbar.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = listView
        recyclerView.setPadding(
            PADDING_PLAIN,
            PADDING_TOP,
            PADDING_PLAIN,
            PADDING_PLAIN)
    }

    companion object {
        private const val PADDING_PLAIN = 0
        private const val PADDING_TOP = 160
    }
}