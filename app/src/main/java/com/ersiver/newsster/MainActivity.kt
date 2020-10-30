package com.ersiver.newsster

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.ersiver.newsster.databinding.ActivityMainBinding
import com.ersiver.newsster.util.PREF_MODE_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_container)
        toolbar = binding.toolbar

        manageToolbarVisibility()

        /**
         * Ensures that the SharedPreferences file is properly initialized with
         * the default values when this method is called for the first time.
         */
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false)
        setupMode()
    }

    /**
     * To create custom toolbar in each fragment
     * (e.g. collapsing toolbar in ArticleFragment) the
     * main_app_bar will be visible to SettingsFragment only.
     */
    private fun manageToolbarVisibility() {
        navController.addOnDestinationChangedListener { _, navDestination, _ ->
            when (navDestination.id) {
                R.id.articleListFragment -> toolbar.visibility = View.GONE
                R.id.articleFragment -> toolbar.visibility = View.GONE
                R.id.settingsFragment -> {
                    toolbar.visibility = View.VISIBLE
                    setToolbarInSettingsFragment()
                }
            }
        }
    }

    private fun setToolbarInSettingsFragment() {
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_back)
            title = resources.getString(R.string.action_settings)
            navigationContentDescription = resources.getString(R.string.nav_up)
            setNavigationOnClickListener { navController.navigateUp() }
        }
    }

    /**
     * Get the user's mode settings from SharedPreferences.
     */
    private fun setupMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val isNightMode = sharedPreferences.getBoolean(PREF_MODE_KEY, false)
        setDefaultNightMode(if (isNightMode) MODE_NIGHT_YES else MODE_NIGHT_NO)
    }
}