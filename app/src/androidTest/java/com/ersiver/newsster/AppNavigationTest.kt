package com.ersiver.newsster

import android.view.View
import androidx.appcompat.widget.MenuPopupWindow.MenuDropDownListView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.ersiver.newsster.di.RepositoryModule
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.ui.MainActivity
import com.ersiver.newsster.util.EspressoUriIdlingResource
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.CoreMatchers.anything
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

/**
 * Large instrumented test for navigation between screens.
 */
@RunWith(AndroidJUnit4::class)
@LargeTest
@UninstallModules(RepositoryModule::class)
@HiltAndroidTest
class AppNavigationTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: NewssterRepository

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Before
    fun registerIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoUriIdlingResource.uriIdlingResource)
    }

    @After
    fun unregisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(EspressoUriIdlingResource.uriIdlingResource)
    }

    /**
     * Test, to check that when the action item "settings" in the overflow
     * menu is clicked, then we navigate to the SettingsFragment.
     */
    @Test
    fun homeScreen_clickOnSettings_navigateToSettings() {
        //Start up list screen.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Open the the overflow menu.
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())

        //Click the item.
        onData(anything())
            .inRoot(RootMatchers.isPlatformPopup())
            .inAdapterView(instanceOf(MenuDropDownListView::class.java))
            .atPosition(0) // for the first submenu item, here: settings
            .perform(click())

        //Verify navigation to Settings.
        onView(withId(R.id.article_list)).check(doesNotExist())
        onView(withText("Switch mode")).check(matches(isDisplayed()))
        onView(withText("Choose language")).check(matches(isDisplayed()))

        activityScenario.close()
    }

    /**
     * Test, to check hat when the the article on the list
     * is clicked, then we navigate to ArticleFragment.
     */
    @Test
    fun homScreen_clickOnListItem_navigateToArticle() {
        //Start up list screen.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Click on the first article on the list.
        onView(withId(R.id.article_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        //Verify navigation to Article.
        onView(withId(R.id.article_list)).check(doesNotExist())
        onView(withId(R.id.description)).check(matches(isDisplayed()))
        onView(withId(R.id.source_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.content)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    /**
     * Test to check that, if the "<-" in the ArticleFragment
     * is clicked, then we navigate back to the list screen.
     */
    @Test
    fun articleFragment_upButton_navigateToHomeScreen() {
        //Start up list screen.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Navigate to Article and click UpButton.
        onView(withId(R.id.article_list))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withContentDescription("Navigate up")).perform(click())

        //Verify navigation to list screen
        onView(withId(R.id.home_toolbar)).check(matches(isDisplayed()))

        activityScenario.close()
    }

    /**
     * Test to check that, if the system back button in the Settings
     * is clicked, then we navigate back to the list screen.
     */
    @Test
    fun settingFragment_backButton_navigateToHomeScreen() {
        //Start up list screen.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        //Navigate to settings
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())
        onData(anything())
            .inRoot(RootMatchers.isPlatformPopup())
            .inAdapterView(instanceOf(MenuDropDownListView::class.java))
            .atPosition(0) // for the first submenu item, here: settings
            .perform(click())

        //In Settings fragment click back btn.
        pressBack()

        //Verify navigation to list screen
        onView(withId(R.id.home_toolbar)).check(matches(isDisplayed()))

        activityScenario.close()
    }
}