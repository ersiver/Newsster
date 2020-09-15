package com.ersiver.newsster.ui.list

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.MenuPopupWindow
import androidx.paging.ExperimentalPagingApi
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.ersiver.newsster.R
import com.ersiver.newsster.di.RepositoryModule
import com.ersiver.newsster.util.EspressoUriIdlingResource
import com.ersiver.newsster.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.instanceOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@ExperimentalPagingApi
@UninstallModules(RepositoryModule::class)
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ArticleListFragmentTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun registerIdlingResource(){
        IdlingRegistry.getInstance().register(EspressoUriIdlingResource.uriIdlingResource)
    }

    @After
    fun unregister(){
        IdlingRegistry.getInstance().unregister(EspressoUriIdlingResource.uriIdlingResource)
    }

    /**
     * Test to check, that when the fragment launches,
     * then the list of news is displayed on the screen.
     */
    @Test
    fun start_showNews(){
        launchFragmentInHiltContainer<ArticleListFragment>(Bundle(), R.style.NewssterTheme)
        onView(withId(R.id.article_list)).check(matches(isDisplayed()))
    }

    /**
     * Test to verify that, when the MenuItem "category"
     * is clicked, then the dialog is shown.
     */
    @Test
    fun categoryMenuItemClicked_dialogShown(){
        launchFragmentInHiltContainer<ArticleListFragment>(Bundle(), R.style.NewssterTheme)
        onView(withId(R.id.categories)).perform(click())
        onView(withText("Business")).check(matches(isDisplayed()))
    }

    /**
     * Test to verify that, when overflow menu is open,
     * then the MenuItem "settings" is present.
     */
    @Test
    fun clickOverflowMenu_displayMenuItemSettings(){
        launchFragmentInHiltContainer<ArticleListFragment>(Bundle(), R.style.NewssterTheme)

        //Open the the overflow menu.
        openActionBarOverflowOrOptionsMenu(ApplicationProvider.getApplicationContext())

        //Verify that the setting item is displayed.
        onData(CoreMatchers.anything())
            .inRoot(RootMatchers.isPlatformPopup())
            .inAdapterView(instanceOf<View>(MenuPopupWindow.MenuDropDownListView::class.java))
            .atPosition(0) // for the first submenu item, here: settings
            .check(matches(isDisplayed()))
    }
}