package com.ersiver.newsster.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import com.ersiver.newsster.MainCoroutinesRule
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.utils.TestUtil
import com.ersiver.newsster.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

/**
 * Unit tests for the implementation of [ArticleListViewModel]
 */

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class ArticleListViewModelTest {
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: NewssterRepository
    private lateinit var viewModel: ArticleListViewModel
    private val article = TestUtil.createArticle()

    @Before
    fun setUp() {
        repository = Mockito.mock(NewssterRepository::class.java)
        viewModel = ArticleListViewModel(repository, SavedStateHandle())
    }

    @Test
    fun openArticleTest() {
        viewModel.openArticle(article)
        val event = viewModel.navigateToArticleEvent.getOrAwaitValue()
        assertThat(
            event.getContentIfNotHandled(),
            `is`(article)
        )
    }

    @Test
    fun updateCategoryTest() {
        viewModel.updateCategory("sport")
        val value = viewModel.categoryLiveData.getOrAwaitValue()
        assertThat(value, `is`("sport"))
    }

    @Test
    fun updateLanguageTest() {
        viewModel.updateLanguage("gb")
        val value = viewModel.languageLiveData.getOrAwaitValue()
        assertThat(value, `is`("gb"))
    }

    @Test
    fun saveCategoryGetSavedCategory() {
        viewModel.saveCategoryFiltering("sport")
        val value = viewModel.getLastSavedCategory()
        assertThat(value, `is`("sport"))
    }

    @Test
    fun saveLanguageGetSavedLanguage() {
        viewModel.saveLanguageFiltering("gb")
        val value = viewModel.getLastSavedLanguage()
        assertThat(value, `is`("gb"))
    }
}