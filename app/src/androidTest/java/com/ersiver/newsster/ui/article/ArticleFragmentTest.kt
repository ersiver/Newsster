package com.ersiver.newsster.ui.article

import android.os.Bundle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ersiver.newsster.R
import com.ersiver.newsster.di.RepositoryModule
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.ui.list.ArticleListFragment
import com.ersiver.newsster.util.EspressoIdlingResource
import com.ersiver.newsster.util.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ArticleFragmentTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    /**
     * Test to verify that, when the fragment
     * launches, the Article is present.
     */
    @Test
    fun test() {
       //TODO
    }
}