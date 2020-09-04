package com.ersiver.newsster.ui.article

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Integration test for the ArticleFragment. To be completed.
 */

@MediumTest
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class ArticleFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun init() {
        // Populate @Inject fields in test class
        //hiltRule.inject()

    }
}
