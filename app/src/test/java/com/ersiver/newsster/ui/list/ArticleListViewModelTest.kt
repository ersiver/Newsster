package com.ersiver.newsster.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.paging.ExperimentalPagingApi
import com.ersiver.newsster.MainCoroutinesRule
import com.ersiver.newsster.api.NewssterService
import com.ersiver.newsster.db.NewssterDatabase
import com.ersiver.newsster.repository.NewssterRepository
import com.ersiver.newsster.utils.TestUtil
import com.ersiver.newsster.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class ArticleListViewModelTest {
    @get:Rule
    var coroutinesRule = MainCoroutinesRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var repository: NewssterRepository
    lateinit var viewModel: ArticleListViewModel

    private val article = TestUtil.createArticle()

    private val database = Mockito.mock(NewssterDatabase::class.java, Mockito.RETURNS_DEEP_STUBS)
    private val service = Mockito.mock(NewssterService::class.java)

    @Before
    fun setUp() {
        repository = NewssterRepository(service, database)
        viewModel = ArticleListViewModel(repository, SavedStateHandle())
    }

    @Test
    fun openArticleTest() {
        viewModel.openArticle(article)
        val event = viewModel.navigateToArticleEvent.getOrAwaitValue()
        assertThat(
            event.getContentIfNotHandled(),
            `is`(notNullValue())
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
        viewModel.updateCategory("gb")
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
    fun saveLanguageGetSavedLanguage(){
        viewModel.saveLanguageFiltering("gb")
        val value = viewModel.getLastSavedLanguage()
        assertThat(value, `is`("gb"))
    }
}