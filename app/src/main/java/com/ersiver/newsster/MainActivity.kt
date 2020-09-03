package com.ersiver.newsster

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import com.ersiver.newsster.databinding.ActivityMainBinding
import com.ersiver.newsster.util.PREF_LANGUAGE_KEY
import com.ersiver.newsster.util.PREF_MODE_KEY
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Ensures that the SharedPreferences file is
         * properly initialized with the default values
         * when this method is called for the first time.
         */
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)

        setupMode()

    }

    /**
     * Get the user's mode settings from SharedPreferences.
     */
    private fun setupMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        val isNightMode = sharedPreferences.getBoolean(PREF_MODE_KEY, false)
        if (isNightMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}